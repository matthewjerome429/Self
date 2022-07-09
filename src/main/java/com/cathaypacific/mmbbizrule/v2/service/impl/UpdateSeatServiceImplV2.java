package com.cathaypacific.mmbbizrule.v2.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cathaypacific.mmbbizrule.handler.PnrCprMergeHelper;
import com.cathaypacific.mmbbizrule.util.CollectionUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.constants.OneAErrorConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.common.TempSeat;
import com.cathaypacific.mbcommon.token.MbTokenCacheLockRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.token.TokenLockKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.RemarkInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.oneaservice.handler.OneAErrorHandler;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.updateseat.UpdateSeatRequestBuilder;
import com.cathaypacific.mmbbizrule.v2.dto.request.updateseat.PaxSeatDetailDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.updateseat.UpdateSeatRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.service.UpdateSeatServiceV2;
import com.cathaypacific.olciconsumer.model.request.allocateSeat.AllocateSeatRequestDTO;
import com.cathaypacific.olciconsumer.model.request.changeseat.ChangeSeatFlightDTO;
import com.cathaypacific.olciconsumer.model.request.changeseat.ChangeSeatPassengerDTO;
import com.cathaypacific.olciconsumer.model.request.changeseat.ChangeSeatRequestDTO;
import com.cathaypacific.olciconsumer.model.response.allocateSeat.AllocateSeatResponseDTO;
import com.cathaypacific.olciconsumer.model.response.changeseat.ChangeSeatResponseDTO;
import com.cathaypacific.olciconsumer.service.client.OlciClient;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;

@Service
public class UpdateSeatServiceImplV2 implements UpdateSeatServiceV2{

	private static LogAgent logger = LogAgent.getLogAgent(UpdateSeatServiceImplV2.class);
	
	@Autowired
	OlciClient olciClient;
	
	@Autowired
	private MbTokenCacheLockRepository mbTokenCacheLockRepository;
	
	@Autowired
	private OneAErrorHandler oneAErrorHandler;
	
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	
	private final static String CHANGE_SEAT_IGNORE_ERROR = "W12Z00235";
	
	@Override
	public AllocateSeatResponseDTO allocateSeat(AllocateSeatRequestDTO requestDTO, String rloc) {
		ResponseEntity<AllocateSeatResponseDTO> responseEntity = olciClient.allocateSeat(requestDTO, rloc, null);

		return responseEntity.getBody();
	}
	
	@Override
	public void saveTempSeat(UpdateSeatRequestDTOV2 request, Booking bookingBefore) throws BusinessBaseException {
		// related passengerSegments
		List<PassengerSegment> passengerSegments = null; 
		if (!CollectionUtils.isEmpty(bookingBefore.getPassengerSegments())) {
			passengerSegments = bookingBefore.getPassengerSegments().stream()
					.filter(ps -> request.getSegmentId().equals(ps.getSegmentId()) && request.getPaxSeatDetails()
							.stream().anyMatch(pax -> ObjectUtils.equals(pax.getPassengerID(), ps.getPassengerId())))
					.collect(Collectors.toList());
		}
		
		// get mask info of current token
		@SuppressWarnings("unchecked")
		List<TempSeat> tempSeats = mbTokenCacheLockRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.TEMP_SEAT, TokenLockKeyEnum.MMB_TEMP_SEAT, request.getRloc(), ArrayList.class);

		if (tempSeats == null) {
			tempSeats = new ArrayList<>();
		}
		
		String segmentId = request.getSegmentId();	
		for (PaxSeatDetailDTOV2 seatDetail : request.getPaxSeatDetails()) {
			updateTempSeat(request, passengerSegments, tempSeats, segmentId, seatDetail);
		}
		mbTokenCacheLockRepository.add(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.TEMP_SEAT, TokenLockKeyEnum.MMB_TEMP_SEAT, request.getRloc(), tempSeats);
	}

	/**
	 * update temp seat
	 * @param request
	 * @param passengerSegments
	 * @param tempSeats
	 * @param segmentId
	 * @param seatDetail
	 */
	private void updateTempSeat(UpdateSeatRequestDTOV2 request, List<PassengerSegment> passengerSegments,
			List<TempSeat> tempSeats, String segmentId, PaxSeatDetailDTOV2 seatDetail) {
		String passengerId = seatDetail.getPassengerID();
		// find selected seat by passengerId and segmentId
		TempSeat tempSeat = tempSeats.stream().filter(seatInfo -> passengerId.equals(seatInfo.getPassengerId()) && segmentId.equals(seatInfo.getSegmentId())).findFirst().orElse(null);
		
		// remove existing temp seat
		if (StringUtils.isEmpty(seatDetail.getSeatNo()) && StringUtils.isEmpty(seatDetail.getSeatPreference())) {
			if (tempSeat != null) {
				tempSeats.remove(tempSeat);
			}
		} else {
			SeatDetail originalSeat = null;
			originalSeat = CollectionUtils.isEmpty(passengerSegments) ? null : passengerSegments.stream()
					.filter(ps -> ObjectUtils.equals(ps.getPassengerId(), seatDetail.getPassengerID())
							&& ObjectUtils.equals(ps.getSegmentId(), segmentId) && ps.getOriginalSeat() != null
							&& !StringUtils.isEmpty(ps.getOriginalSeat().getSeatNo()))
					.map(PassengerSegment :: getOriginalSeat).findFirst().orElse(null);
			
			if (tempSeat == null) {
				tempSeat = createTempSeat(request, segmentId, seatDetail, passengerId, originalSeat);
				tempSeats.add(tempSeat);
			}
			
			// if the updated seat is same with original seat, then just remove the temp seat record from Redis
			if (!StringUtils.isEmpty(tempSeat.getOriginalSeatNo()) && tempSeat.getOriginalSeatNo().equals(seatDetail.getSeatNo())) {
				tempSeats.remove(tempSeat);
			} else if (!StringUtils.isEmpty(seatDetail.getSeatNo())) {
				tempSeat.setSeatNo(seatDetail.getSeatNo());
				tempSeat.setExlSeat(seatDetail.isExlSeat());
			} else {
				tempSeat.setSeatPreference(seatDetail.getSeatPreference());
			}
		}
	}

	/**
	 * create a temp seat
	 * @param request
	 * @param segmentId
	 * @param seatDetail
	 * @param passengerId
	 * @param originalSeat
	 */
	private TempSeat createTempSeat(UpdateSeatRequestDTOV2 request, String segmentId, PaxSeatDetailDTOV2 seatDetail,
			String passengerId, SeatDetail originalSeat) {
		TempSeat tempSeat = new TempSeat();
		tempSeat.setJourneyId(request.getJourneyId());
		tempSeat.setPassengerId(passengerId);
		tempSeat.setSegmentId(segmentId);
		tempSeat.setOriginalSeatNo(originalSeat == null ? null : originalSeat.getSeatNo());
		tempSeat.setForfeited(forfeitedPaidExl(seatDetail, originalSeat));
		return tempSeat;
	}

	/**
	 * if the seat has been forfeited from paid EXL to regular seat
	 * @param seatDetail
	 * @param originalSeat
	 * @return boolean
	 */
	private boolean forfeitedPaidExl(PaxSeatDetailDTOV2 seatDetail, SeatDetail originalSeat) {
		return originalSeat != null && BooleanUtils.isTrue(originalSeat.isExlSeat())
				&& (!originalSeat.isFromDCS() && BooleanUtils.isTrue(originalSeat.isPaid())
						|| originalSeat.isFromDCS() && MMBBizruleConstants.SEAT_PAYMENT_STATUS_PAID.equals(originalSeat.getPaymentStatus()))
				&& !seatDetail.isExlSeat();
	}

	@Override
	public ChangeSeatResponseDTO changeSeat(UpdateSeatRequestDTOV2 requestDTO, List<Journey> cprJourneys) throws BusinessBaseException {
		ChangeSeatRequestDTO request = buildRequest(requestDTO, cprJourneys);
		
		ResponseEntity<ChangeSeatResponseDTO> responseEntity = olciClient.changeSeat(request, requestDTO.getRloc(), null);

		List<ErrorInfo> errorInfos = getChangeSeatErrorInfos(request, responseEntity.getBody());

		if (!responseEntity.getStatusCode().equals(HttpStatus.OK) || responseEntity.getBody() == null || errorInfos != null) {
			throw new ExpectedException(String.format("Change seat failed for booking: %s", requestDTO.getRloc()), errorInfos.get(0));
		}
		return responseEntity.getBody();
	}
	
	@Override
	public void addRemark(List<RemarkInfo> remarkInfos, String rloc) throws BusinessBaseException {
		UpdateSeatRequestBuilder builder = new UpdateSeatRequestBuilder();
		UpdateSeatRequestDTO mockedRequest = mockRequestToAddRemark(rloc);
		PNRAddMultiElements request = builder.buildRequest(mockedRequest, null, null, remarkInfos, null);
		OneaResponse<PNRReply> pnrReplay = mmbOneAWSClient.addMultiElements(request, null);
		
		//check onea error for update
		checkOneAResponseError(pnrReplay.getBody(), pnrReplay.getOneAAction().getInterfaceCode());
	}
	
	/**
	 * check 1A response error
	 * @param pnrReply
	 * @param oneAWsCallCode
	 * @throws BusinessBaseException
	 */
	private void checkOneAResponseError(PNRReply pnrReply,String oneAWsCallCode) throws BusinessBaseException{
		List<OneAError> oneAErrorCodeList = PnrResponseParser.getAllErrors(pnrReply);
		oneAErrorHandler.parseOneAErrorCode(oneAErrorCodeList, MMBConstants.APP_CODE,
				OneAErrorConstants.MMB_ACTION_NO_SPECIFIC, oneAWsCallCode);
	}

	/**
	 * mock request to add remark for the voluntary changed seat
	 * @param rloc
	 * @return UpdateSeatRequestDTO
	 */
	private UpdateSeatRequestDTO mockRequestToAddRemark(String rloc) {
		UpdateSeatRequestDTO request = new UpdateSeatRequestDTO();
		request.setRloc(rloc);
		return request;
	}

	/**
	 * if there's error returned
	 * @param request
	 * @param body
	 * @return List<ErrorInfo>
	 */
	private List<ErrorInfo> getChangeSeatErrorInfos(ChangeSeatRequestDTO request, ChangeSeatResponseDTO body) {
		// check whether there exist an error. If no, return an empty object
		if (body == null || CollectionUtils.isEmpty(body.getErrors())) {
			return null;
		}

		// insert all errors into the errorInfos
		// check errors in the response level
		List<ErrorInfo> errorInfos = Optional.ofNullable(PnrCprMergeHelper.covertErrorInfos(getChangeSeatErrorObjects(body.getErrors()))).orElse(new ArrayList<>());

		// check errors in the passenger level
		if (!CollectionUtils.isEmpty(body.getPassengers())) {
			body.getPassengers().forEach(passenger -> {
				List<ErrorInfo> paxErrorInfos = PnrCprMergeHelper.covertErrorInfos(getChangeSeatErrorObjects(passenger.getErrors()));
				if (paxErrorInfos != null && CollectionUtils.isEmpty(paxErrorInfos)) {
					errorInfos.addAll(paxErrorInfos);
				}
			});
		}
		return errorInfos;
	}

	// there's error returned in response or in each passenger, ignore "W12Z00235" error
	private List<com.cathaypacific.olciconsumer.model.response.ErrorInfo> getChangeSeatErrorObjects(List<com.cathaypacific.olciconsumer.model.response.ErrorInfo> errorInfos) {
		// return an empty list if an empty list is passed
		if (errorInfos == null) return new ArrayList<>();
		// filter out all empty or whitelisted change seat errors from the list
		return errorInfos.stream().filter(error -> error != null && !CHANGE_SEAT_IGNORE_ERROR.equals(error.getErrorCode())).collect(Collectors.toList());
	}

	/**
	 * build request for change seat
	 * @param requestDTO
	 * @param cprJourneys
	 * @return ChangeSeatRequestDTO
	 * @throws BusinessBaseException 
	 */
	private ChangeSeatRequestDTO buildRequest(UpdateSeatRequestDTOV2 requestDTO, List<Journey> cprJourneys) throws BusinessBaseException {
		ChangeSeatRequestDTO changeSeatRequest = new ChangeSeatRequestDTO();
		changeSeatRequest.setJourneyId(requestDTO.getJourneyId());
		List<ChangeSeatPassengerDTO> changeSeatPassengers = new ArrayList<>();
		String segmentId = requestDTO.getSegmentId();
		for (PaxSeatDetailDTOV2 seatDetail : requestDTO.getPaxSeatDetails()) {
			String passengerId = seatDetail.getPassengerID();
			CprJourneyPassengerSegment cprPassengerSegment = getCprPassengerSegment(cprJourneys, passengerId, segmentId, requestDTO.getJourneyId());
			if (cprPassengerSegment == null) {
				throw new ExpectedException(String.format("Cannot Update seat info because retrieve journey info failed, rloc: %s, passengerId: %s, segmentId: %s", requestDTO.getRloc(), passengerId, segmentId), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
			}
			
			ChangeSeatPassengerDTO changeSeatPassenger = new ChangeSeatPassengerDTO();
			List<ChangeSeatFlightDTO> changeSeatFlights = new ArrayList<>();
			ChangeSeatFlightDTO changeSeatFlight = new ChangeSeatFlightDTO();
			changeSeatFlight.setExitSeat(seatDetail.isExitSeat());
			changeSeatFlight.setExtraLegRoomSeat(seatDetail.isExlSeat());
			changeSeatFlight.setProductIdentifierDID(cprPassengerSegment.getCprProductIdentifierDID());
			changeSeatFlight.setSeatNum(seatDetail.getSeatNo());
			changeSeatFlights.add(changeSeatFlight);
			
			changeSeatPassenger.setFlights(changeSeatFlights);
			changeSeatPassenger.setUniqueCustomerId(cprPassengerSegment.getCprUniqueCustomerId());
			
			changeSeatPassengers.add(changeSeatPassenger);
		}
		changeSeatRequest.setPassengers(changeSeatPassengers);
		return changeSeatRequest;
	}

	/**
	 * get cpr passenger Segment by passengerId and segmentId
	 * @param cprJourneys
	 * @param passengerId
	 * @param segmentId
	 * @param journeyId 
	 * @return CprJourneyPassengerSegment
	 */
	private CprJourneyPassengerSegment getCprPassengerSegment(List<Journey> cprJourneys, String passengerId, String segmentId, String journeyId) {
		if (StringUtils.isEmpty(passengerId) || StringUtils.isEmpty(segmentId) || StringUtils.isEmpty(journeyId)) {
			return null;
		}
		
		Journey journey = cprJourneys.stream().filter(jour-> journeyId.equals(jour.getJourneyId())).findFirst().orElse(null);
		if (journey != null && !CollectionUtils.isEmpty(journey.getPassengerSegments())) {
			return journey.getPassengerSegments().stream().filter(ps -> passengerId.equals(ps.getPassengerId()) && segmentId.equals(ps.getSegmentId())).findFirst().orElse(null);
		} else {
			return null;
		}
	}
}
