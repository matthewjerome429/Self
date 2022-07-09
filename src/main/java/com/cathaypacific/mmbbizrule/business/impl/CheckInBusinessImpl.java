package com.cathaypacific.mmbbizrule.business.impl;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.common.TempSeat;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheLockRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.token.TokenLockKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.business.AdcErrorMessageEmailService;
import com.cathaypacific.mmbbizrule.business.CheckInBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OLCIConstants;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.service.EcommService;
import com.cathaypacific.mmbbizrule.cxservice.olci.service.SentBPService;
import com.cathaypacific.mmbbizrule.dto.common.booking.DepartureArrivalTimeDTO;
import com.cathaypacific.mmbbizrule.dto.request.checkin.accept.CheckInAcceptRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.checkin.cancel.CancelCheckInRequestDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.RemarkInfo;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptCprJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptCprSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptPassengerSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.boardingpass.SendBoardingPassResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancancel.CanCancelCheckInDetailResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancancel.CanCancelCheckInPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancancel.CanCancelCheckInSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancel.CancelCheckInCprJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancel.CancelCheckInPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancel.CancelCheckInResponseDTOV2;
import com.cathaypacific.mmbbizrule.dto.response.checkin.cancel.CancelCheckInSegmentDTO;
import com.cathaypacific.mmbbizrule.handler.PnrCprMergeHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.ContactInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneySegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.v2.handler.CacheHelper;
import com.cathaypacific.mmbbizrule.v2.service.UpdateSeatServiceV2;
import com.cathaypacific.olciconsumer.model.request.FlightInfoDTO;
import com.cathaypacific.olciconsumer.model.request.PassengerInfoDTO;
import com.cathaypacific.olciconsumer.model.request.allocateSeat.AllocateSeatRequestDTO;
import com.cathaypacific.olciconsumer.model.request.boardingpass.SendEmailRequestDTO;
import com.cathaypacific.olciconsumer.model.request.boardingpass.SendSMSRequestDTO;
import com.cathaypacific.olciconsumer.model.request.cancelcheckin.CancelCheckInRequestDTO;
import com.cathaypacific.olciconsumer.model.request.checkin.CheckInRequestDTO;
import com.cathaypacific.olciconsumer.model.response.BaseResponseDTO;
import com.cathaypacific.olciconsumer.model.response.FlightDTO;
import com.cathaypacific.olciconsumer.model.response.JourneyDTO;
import com.cathaypacific.olciconsumer.model.response.PassengerDTO;
import com.cathaypacific.olciconsumer.model.response.SeatDTO;
import com.cathaypacific.olciconsumer.model.response.allocateSeat.AllocateSeatResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.BoardingPassResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.SendEmailResponseDTO;
import com.cathaypacific.olciconsumer.model.response.boardingpass.SendSMSResponseDTO;
import com.cathaypacific.olciconsumer.model.response.cancelcheckin.CancelCheckInResponseDTO;
import com.cathaypacific.olciconsumer.model.response.checkin.CheckInResponseDTO;
import com.cathaypacific.olciconsumer.service.client.OlciClient;

@Service
public class CheckInBusinessImpl implements CheckInBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(CheckInBusinessImpl.class);
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private BookingBuildService bookingBuildService;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private PnrCprMergeHelper pnrCprMergeHelper;
	
	@Autowired
	private UpdateSeatServiceV2 updateSeatServiceImplV2;
	
	@Autowired
	private OlciClient olciClient;

	@Autowired
	private SentBPService sentBPService;
	
	@Autowired
	private CacheHelper cacheHelper;
	
	@Autowired
	private AdcErrorMessageEmailService adcErrorMessageEmailService;
	
	@Autowired
	private EcommService ecommService;
	
	@Autowired
	private MbTokenCacheLockRepository mbTokenCacheLockRepository;
	
	@Value("${olci.adcerrormessage.email.enabled}")
	private boolean adcErrorMessageEmailEnabled;
	
	
	@Override
	public CheckInAcceptResponseDTO accept(LoginInfo loginInfo, CheckInAcceptRequestDTO requestDTO) throws BusinessBaseException {
		String rloc = requestDTO.getRloc();
		
		/** Get RetrievePnrBooking */
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		
		/** Check retrievePnrBooking is valid */ 
		checkRetrievePnrBooking(retrievePnrBooking, rloc, loginInfo);
		
		/** Build booking merged CPR */
	 	Booking bookingMergedCpr = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, buildBookingBuildRequired());
	 	
	 	/** Check and get Journey by journeyId from booking */
	 	Journey cprJourney = getAndCheckCprJourneyById(bookingMergedCpr, requestDTO.getJourneyId());
	 	
	 	/** Check & build request for check-in */
	 	CheckInRequestDTO checkInRequestDTO = buildCheckInAcceptRequest(requestDTO, cprJourney);
	 	
	 	/** Assign seat */
	 	List<TempSeat> tempSeat = getSeatsByToken(requestDTO.getRloc());
	 	AllocateSeatResponseDTO allocateSeatResponseDTO = assignSeat(bookingMergedCpr, requestDTO, tempSeat);
	 	
	 	/** if Assign seat failure, covert CPR errorInfos with type "S" to MMB errorInfos, and return response */	
	 	List<ErrorInfo> allocateSeatErrorInfos = getAndCovertErrorInfosByType(allocateSeatResponseDTO, OLCIConstants.CPR_ERROR_INFO_TYPE_S);
		if(CollectionUtils.isNotEmpty(allocateSeatErrorInfos)) {
			return new CheckInAcceptResponseDTO(allocateSeatErrorInfos, false);
		} else {
			// delete temp seat cache when allocate seat success.
			mbTokenCacheLockRepository.delete(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.TEMP_SEAT, TokenLockKeyEnum.MMB_TEMP_SEAT, rloc);
			try {
				// if user had a paid EXL seat before check in and changes the seat to a regular seat while check in, add remark to PNR
				addRemarkForSeat(bookingMergedCpr, tempSeat);
			} catch (Exception e) {
				logger.warn("Failed to add remark of voluntary seat change", e);
			}
		}
		
		/** Do check-in operation */
	 	CheckInResponseDTO checkInResponseDTO = acceptCheckIn(checkInRequestDTO, retrievePnrBooking.getOneARloc());
	 	if(checkInResponseDTO == null) {
	 		return new CheckInAcceptResponseDTO(new ErrorInfo(ErrorCodeEnum.ERR_CHECK_IN_ACCEPTANCE_FAIL));
	 	}
	 	
	 	/** 
	 	 * If check-in failure, covert CPR errorInfos to MMB errorInfos, and return response
	 	 */
	 	List<ErrorInfo> checkInErrorInfos = getAndCovertErrorInfosByType(checkInResponseDTO, null);
	 	
	 	/**
	 	 * Get booking type just for tagging
	 	 * */
	 	String bookingTypeForTagging = getBookingTypeForTagging(bookingMergedCpr);
	 	
	 	// If standby error, return response with standby errors
	 	List<ErrorInfo> checkInStandByErrors = getErrorInfosByFieldName(checkInErrorInfos, OLCIConstants.CPR_ERROR_FIELD_NAME_STANDBY);
		if(CollectionUtils.isNotEmpty(checkInStandByErrors)) {
			tagging(bookingTypeForTagging, rloc, "Standby");
			return new CheckInAcceptResponseDTO(checkInStandByErrors);
		}
		
		// If Reject error = all passenger not accept checked in all segments, return response with all infos with checkedIn = false
		List<ErrorInfo> checkInRejectErrors = getErrorInfosByFieldName(checkInErrorInfos, OLCIConstants.CPR_ERROR_FIELD_NAME_REJECT);
	 	if(CollectionUtils.isNotEmpty(checkInRejectErrors)) {
	 		tagging(bookingTypeForTagging, rloc, "Rejected");
			return generateCheckInRejectResponse(cprJourney);
		}
		
		CheckInAcceptResponseDTO checkInAcceptResponse = new CheckInAcceptResponseDTO();
		checkInAcceptResponse.setHasAcceptedSectorOfRequest(checkInResponseDTO.isHasAcceptedSectorOfRequest());
		checkInAcceptResponse.setCprJourney(buildCheckInAcceptCprJourney(cprJourney, checkInResponseDTO.getJourney(), bookingMergedCpr));
		checkInAcceptResponse.setErrors(PnrCprMergeHelper.covertErrorInfos(checkInResponseDTO.getErrors()));
		
		// OLSS-7536 tagging
		if(BooleanUtils.isTrue(checkInAcceptResponse.isHasAcceptedSectorOfRequest())) {
			tagging(bookingTypeForTagging, rloc, "Accepted");
		}
		
		/** Add back original seat and handle allocate seat error */
		combineAllocateSeatResponse(checkInAcceptResponse, allocateSeatResponseDTO, tempSeat, bookingMergedCpr, cprJourney);

		/** Auto send boarding pass after check in success */
		if (checkInResponseDTO.isHasAcceptedSectorOfRequest()) {
			sendBoardingPass(checkInAcceptResponse, requestDTO, bookingMergedCpr);
		}
		
		/** Auto send email : ADC error message */
		if (adcErrorMessageEmailEnabled  && CollectionUtils.isNotEmpty(cacheHelper.getAdcMessageFromCache(rloc))) {
			if (logger.isDebugEnabled()) {
				logger.debug("adcErrorMessageEmailEnabled: true");	
			}
			adcErrorMessageEmailService.sendEmail(cacheHelper.getAdcMessageFromCache(rloc), checkInAcceptResponse, requestDTO, bookingMergedCpr);
		}
		
		// Clear AEP products cache after check in
		if (checkInResponseDTO.isHasAcceptedSectorOfRequest()) {
			ecommService.removeEcommCacheProducts(rloc, loginInfo.getMmbToken());
		}

		return checkInAcceptResponse;
	}

	/**
	 * OLSS-7536 tagging
	 * 
	 * @param bookingTypeForTagging
	 * @param rloc
	 * @param checkInStatus
	 */
	private void tagging(String bookingTypeForTagging, String rloc, String checkInStatus) {
		if(StringUtils.isNotEmpty(bookingTypeForTagging)) {
			logger.info(String.format("Acceptance %s| %s | RLOC | %s", bookingTypeForTagging, checkInStatus, rloc), true);
		}
	}

	/**
	 * get booking type for tagging
	 * 
	 * @param booking
	 * @return
	 */
	private String getBookingTypeForTagging(Booking booking) {
		if(booking == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		if(BooleanUtils.isNotTrue(booking.isRedemptionBooking())) {
			sb.append("| Revenue ");
		}
		
		if(BooleanUtils.isTrue(booking.isMiceBooking())) {
			sb.append("| Mice ");
		}
		
		if(BooleanUtils.isTrue(booking.isStaffBooking()) || BooleanUtils.isTrue(booking.isCprStaffBooking())) {
			sb.append("| Staff ");
		}
		
		return sb.toString();
	}

	@Override
	public CanCancelCheckInDetailResponseDTO canCancel(LoginInfo loginInfo, String rloc, String journeyId) throws BusinessBaseException {
		/** Get RetrievePnrBooking */
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		
		/** Check retrievePnrBooking is valid */ 
		checkRetrievePnrBooking(retrievePnrBooking, rloc, loginInfo);
		
		/** Build booking merged CPR */
	 	Booking booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, buildBookingBuildRequired());
	 		
	 	/** Get cprJourney from booking by journey id */
	 	Journey journey = getCprJourneyById(booking, journeyId, rloc);
	 	if(journey == null) {
	 		throw new ExpectedException(String.format("Can cancel check-in failure -> Cannot find request Journey[%s] in booking[%s] or no passengerSegments in this journey.",
					journeyId, rloc), new ErrorInfo(ErrorCodeEnum.ERR_CANCEL_CHECK_IN_JOURNEY_NOT_FOUND));
	 	}
	 	
	 	CanCancelCheckInDetailResponseDTO response = new CanCancelCheckInDetailResponseDTO();
	 	response.setJourneyId(journeyId);
	 	response.setPassengers(buildCanCancelPassengers(booking, journey.getPassengers()));
	 	response.setSegments(buildCanCancelSegments(booking, journey.getSegments()));
	 	return response;
	}
	
	@Override
	public CancelCheckInResponseDTOV2 cancel(LoginInfo loginInfo, CancelCheckInRequestDTOV2 requestDTO) throws BusinessBaseException {
		String rloc = requestDTO.getRloc();
		String journeyId = requestDTO.getJourneyId();
		
		/** Get RetrievePnrBooking */
		RetrievePnrBooking retrievePnrBooking = pnrInvokeService.retrievePnrByRloc(rloc);
		
		/** Check retrievePnrBooking is valid */ 
		checkRetrievePnrBooking(retrievePnrBooking, rloc, loginInfo);
		
		/** Build booking merged CPR */
	 	Booking booking = bookingBuildService.buildBooking(retrievePnrBooking, loginInfo, buildBookingBuildRequired());
		
		/** Get cprJourney from booking by journey id */
	 	Journey journey = getCprJourneyById(booking, journeyId, rloc);
	 	if(journey == null) {
	 		throw new ExpectedException(String.format("Cancel check-in failure -> Cannot find request Journey[%s] in booking[%s] or no passengerSegments in this journey.",
					journeyId, rloc), new ErrorInfo(ErrorCodeEnum.ERR_CANCEL_CHECK_IN_JOURNEY_NOT_FOUND));
	 	}
	 	
	 	/** Build cancel check-in requestDTO  */
	 	CancelCheckInRequestDTO cancelCheckInRequestDTO = buildCancelCheckInRequest(requestDTO, journey);
	 	
	 	/** Do cancel Check-in operation */
	 	CancelCheckInResponseDTO cancelCheckInResponseDTO = doCancel(cancelCheckInRequestDTO, booking.getOneARloc());
	 	if(cancelCheckInResponseDTO == null) {
	 		throw new ExpectedException(String.format("Cancel check-in failure -> Cancel journey[%s] booking[%s] check-in failure when calling OLCI",
					journeyId, rloc), new ErrorInfo(ErrorCodeEnum.ERR_CANCEL_CHECK_IN_FAILURE));
	 	}
	 	
	 	/** 
	 	 * If cancel check-in failure, covert CPR errorInfos to MMB errorInfos, and return response
	 	 */
	 	List<ErrorInfo> stopErrorInfos = getAndCovertErrorInfosByType(cancelCheckInResponseDTO, OLCIConstants.CPR_ERROR_INFO_TYPE_S);
	 	if(CollectionUtils.isNotEmpty(stopErrorInfos)) {
	 		return new CancelCheckInResponseDTOV2(stopErrorInfos);
	 	}
	 	
	 	CancelCheckInResponseDTOV2 cancelCheckInResponse = new CancelCheckInResponseDTOV2();
	 	cancelCheckInResponse.setCprJourney(buildCancelCheckInCprJourney(booking, journey, cancelCheckInResponseDTO.getJourney(), requestDTO.getPassengerIds()));
	 	cancelCheckInResponse.setAnyPassengersCancelledSuccess();
	 	cancelCheckInResponse.setErrors(PnrCprMergeHelper.covertErrorInfos(cancelCheckInResponseDTO.getErrors()));
		return cancelCheckInResponse;
	}
	
	/**
	 * Build BookingBuildRequired using CPR cache from OLCI
	 * @return
	 */
	private BookingBuildRequired buildBookingBuildRequired() {
		BookingBuildRequired required = new BookingBuildRequired();
//		required.setUseCprSession(true);
		return required;
	}

	/**
	 * Build Cancel check-in Journey
	 * @param booking 
	 * 
	 * @param journey
	 * @param journeyDTO
	 * @param requestPassengerIds
	 * @return
	 */
	private CancelCheckInCprJourneyDTO buildCancelCheckInCprJourney(Booking booking, Journey journey, JourneyDTO cprJourneyDTO, List<String> requestPassengerIds) {
		if(journey == null || CollectionUtils.isEmpty(journey.getPassengers())
				|| cprJourneyDTO == null || CollectionUtils.isEmpty(cprJourneyDTO.getPassengers())) {
			return null;
		}
		
		CancelCheckInCprJourneyDTO cancelCheckInCprJourneyDTO = new CancelCheckInCprJourneyDTO();
		cancelCheckInCprJourneyDTO.setErrors(PnrCprMergeHelper.covertErrorInfos(cprJourneyDTO.getErrors()));
		cancelCheckInCprJourneyDTO.setJourneyId(journey.getJourneyId());
		cancelCheckInCprJourneyDTO.setPassengers(buildCancelCheckInPassengers(booking, journey.getPassengers(), cprJourneyDTO.getPassengers(), requestPassengerIds));
		cancelCheckInCprJourneyDTO.setSegments(buildCancelCheckInSegments(booking, journey.getSegments()));
		return cancelCheckInCprJourneyDTO;
	}

	/**
	 * Build cancel check-in segments
	 * 
	 * @param booking
	 * @param cprSegments
	 * @return
	 */
	private List<CancelCheckInSegmentDTO> buildCancelCheckInSegments(Booking booking, List<CprJourneySegment> cprSegments) {
		if(booking == null || CollectionUtils.isEmpty(booking.getSegments()) || CollectionUtils.isEmpty(cprSegments)) {
			return null;
		}
		
		List<CprJourneySegment> canCheckInSegments = cprSegments.stream().filter(CprJourneySegment::getCanCheckIn).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(canCheckInSegments)) {
			logger.warn(String.format("buildCancelCheckInSegments -> no canCheckIn segments found in cprJourney, booking[%s]", booking.getOneARloc()));
			return null;
		}
		
		List<CancelCheckInSegmentDTO> cancelCheckInSegments = new ArrayList<>();
		for(CprJourneySegment cprSegment : canCheckInSegments) {
			if(cprSegment == null) {
				continue;
			}
			
			CancelCheckInSegmentDTO cancelCheckInSegment = new CancelCheckInSegmentDTO();
			cancelCheckInSegment.setSegmentId(cprSegment.getSegmentId());
			
			Segment segment = BookingBuildUtil.getSegmentById(booking.getSegments(), cprSegment.getSegmentId());
			if(segment != null) {
				cancelCheckInSegment.setOriginPort(segment.getOriginPort());
				cancelCheckInSegment.setDestPort(segment.getDestPort());
				cancelCheckInSegment.setDepartureTime(covertDepartureArrivalTime(segment.getDepartureTime()));
				cancelCheckInSegment.setArrivalTime(covertDepartureArrivalTime(segment.getArrivalTime()));
				cancelCheckInSegment.setOperateSegmentNumber(segment.getOperateSegmentNumber());
				cancelCheckInSegment.setMarketSegmentNumber(segment.getMarketSegmentNumber());
				cancelCheckInSegment.setOperateCompany(segment.getOperateCompany());
				cancelCheckInSegment.setMarketCompany(segment.getMarketCompany());
				cancelCheckInSegment.setStatus(Optional.ofNullable(segment.getSegmentStatus()).orElse(new SegmentStatus()).getStatus());
				cancelCheckInSegment.setAirCraftType(segment.getAirCraftType());
				cancelCheckInSegment.setFlown(segment.isFlown());
			} else {
				logger.warn(String.format("buildCancelCheckInSegments -> can't find segment[id: %s] in PNR[%s] booking",
						cprSegment.getSegmentId(), booking.getOneARloc()));
			}
			
			cancelCheckInSegments.add(cancelCheckInSegment);
		}
		return cancelCheckInSegments;
	}

	/**
	 * Build cancel check-in passengers
	 * 
	 * @param booking
	 * @param cprPassengers
	 * @param cprPassengerDTOs
	 * @param requestPassengerIds
	 * @return
	 */
	private List<CancelCheckInPassengerDTO> buildCancelCheckInPassengers(Booking booking,
			List<CprJourneyPassenger> cprPassengers, List<PassengerDTO> cprPassengerDTOs, List<String> requestPassengerIds) {
		List<CancelCheckInPassengerDTO> cancelCheckInPassengerDTOs = new ArrayList<>();
		
		for(CprJourneyPassenger cprPassenger : cprPassengers) {
			if(cprPassenger == null) {
				continue;
			}
			
			String passengerId = cprPassenger.getPassengerId();
			String uniqueCustomerId = cprPassenger.getCprUniqueCustomerId();
			
			CancelCheckInPassengerDTO cancelCheckInPassengerDTO = new CancelCheckInPassengerDTO();
			cancelCheckInPassengerDTO.setPassengerId(passengerId);
			cancelCheckInPassengerDTO.setCprUniqueCustomerId(uniqueCustomerId);
			boolean requestedCancelCheckIn = requestPassengerIds.contains(cprPassenger.getPassengerId());
			cancelCheckInPassengerDTO.setRequestedCancelCheckIn(requestedCancelCheckIn);
			
			// Get passenger informations from booking
			Passenger passenger = BookingBuildUtil.getPassengerById(booking.getPassengers(), cprPassenger.getPassengerId());
			if(passenger != null) {
				cancelCheckInPassengerDTO.setParentId(passenger.getParentId());
				cancelCheckInPassengerDTO.setTitle(passenger.getTitle());
				cancelCheckInPassengerDTO.setPassengerType(passenger.getPassengerType());
				cancelCheckInPassengerDTO.setFamilyName(passenger.getFamilyName());
				cancelCheckInPassengerDTO.setGivenName(passenger.getGivenName());
				cancelCheckInPassengerDTO.setLoginPassenger(BooleanUtils.isTrue(passenger.isPrimaryPassenger()));				
			} else {
				logger.warn(String.format("buildCancelCheckInPassengers -> can't find passenger[id:%s, cprId:%s] in booking[%s]",
						passengerId, uniqueCustomerId, booking.getOneARloc()));
			}
			
			// if this passenger is not requested, OLCI will not return this passenger info, get info from old booking
			if(!requestedCancelCheckIn) {
				cancelCheckInPassengerDTO.setCheckedIn(cprPassenger.isCheckedIn());
				cancelCheckInPassengerDTO.setCancelled(false);
				cancelCheckInPassengerDTO.setCanCancelCheckIn(cprPassenger.isCanCancelCheckIn());
				cancelCheckInPassengerDTOs.add(cancelCheckInPassengerDTO);
				continue;
			}
			
			// Get passenger informations from cancel check-in response from OLCI
			PassengerDTO cprPassengerDTO = getPassengerByUniqueCustomerId(cprPassengerDTOs, uniqueCustomerId);
			if(cprPassengerDTO != null) {
				cancelCheckInPassengerDTO.setCheckedIn(cprPassengerDTO.isCheckInAccepted());
				cancelCheckInPassengerDTO.setCancelled(requestedCancelCheckIn && !cprPassengerDTO.isCheckInAccepted());
				cancelCheckInPassengerDTO.setCanCancelCheckIn(!cprPassengerDTO.isInhibitCancelCheckIn());
				cancelCheckInPassengerDTO.setErrors(PnrCprMergeHelper.covertErrorInfos(cprPassengerDTO.getErrors()));				
			} else if(requestedCancelCheckIn) {
				logger.warn(String.format("buildCancelCheckInPassengers -> can't find passenger[id:%s, cprId:%s] from cancel check-in response from OLCI, booking[%s]",
						passengerId, uniqueCustomerId, booking.getOneARloc()));
			}
			
			cancelCheckInPassengerDTOs.add(cancelCheckInPassengerDTO);
		}
		
		return sortCancelCheckInPassengers(cancelCheckInPassengerDTOs);
	}

	/**
	 * Sort cancel check-in passengers.
	 * priority:
	 * 1. adult -> infant
	 * 2. requested passenger
	 * 3. cancelled passenger
	 * 4. passengerId
	 * 5. login passenger
	 * 
	 * @param cancelCheckInPassengerDTOs
	 * @return
	 */
	private List<CancelCheckInPassengerDTO> sortCancelCheckInPassengers(List<CancelCheckInPassengerDTO> cancelCheckInPassengerDTOs) {
		if(CollectionUtils.isEmpty(cancelCheckInPassengerDTOs)) {
			return null;
		}
		
		List<CancelCheckInPassengerDTO> adults = new ArrayList<>(); // Adults
		List<CancelCheckInPassengerDTO> infants = new ArrayList<>(); // Infants without seat
		for(CancelCheckInPassengerDTO cancelCheckInPassengerDTO : cancelCheckInPassengerDTOs) {
			if(cancelCheckInPassengerDTO == null) {
				continue;
			}
			
			if(StringUtils.isNotEmpty(cancelCheckInPassengerDTO.getParentId())) {
				infants.add(cancelCheckInPassengerDTO);
			} else {
				adults.add(cancelCheckInPassengerDTO);
			}
		}
		
		/**
		 * sorting priority: 1. requested passenger 2. cancelled passenger 3.passengerId 4. login passenger
		 * */
		List<CancelCheckInPassengerDTO> sortedAdults = adults.stream()
				.sorted(Comparator.comparing(CancelCheckInPassengerDTO::isRequestedCancelCheckIn, this::compareBoolean))
				.sorted(Comparator.comparing(CancelCheckInPassengerDTO::isCancelled, this::compareBoolean))
				.sorted(Comparator.comparing(CancelCheckInPassengerDTO::getPassengerId))
				.sorted(Comparator.comparing(CancelCheckInPassengerDTO::isLoginPassenger, this::compareBoolean))
				.collect(Collectors.toList());
		
		List<CancelCheckInPassengerDTO> passengers = new ArrayList<>();
		passengers.addAll(sortedAdults);
		passengers.addAll(infants);
		return passengers;
	}

	/**
	 * Get Passenger by UniqueCustomerId
	 * 
	 * @param cprPassengerDTOs
	 * @param uniqueCustomerId
	 * @return
	 */
	private PassengerDTO getPassengerByUniqueCustomerId(List<PassengerDTO> cprPassengerDTOs, String uniqueCustomerId) {
		if(CollectionUtils.isEmpty(cprPassengerDTOs) || StringUtils.isEmpty(uniqueCustomerId)) {
			return null;
		}
		
		return cprPassengerDTOs.stream().filter(passenger -> uniqueCustomerId.equals(passenger.getUniqueCustomerId())).findFirst().orElse(null);
	}

	/**
	 * Do cancel check-in
	 * 
	 * @param cancelCheckInRequestDTO
	 * @param rloc
	 * @return
	 */
	private CancelCheckInResponseDTO doCancel(CancelCheckInRequestDTO cancelCheckInRequestDTO, String rloc) {
		try {
			ResponseEntity<CancelCheckInResponseDTO> responseEntity = olciClient.cancelCheckIn(cancelCheckInRequestDTO, rloc, null);
			return responseEntity.getBody();
		} catch(Exception e) {
			logger.error(String.format("Journey[%s] cancel check-in failure when trying to call OLCI",
					cancelCheckInRequestDTO != null ? cancelCheckInRequestDTO.getJourneyId() : null), e);
		}
		
		return null;
	}

	/**
	 * Build cancel check-in requestDTO
	 * 
	 * @param requestDTO
	 * @param journey
	 * @return
	 * @throws BusinessBaseException
	 */
	private CancelCheckInRequestDTO buildCancelCheckInRequest(CancelCheckInRequestDTOV2 requestDTO, Journey journey) throws BusinessBaseException {
		if(CollectionUtils.isEmpty(journey.getPassengers())) {
			throw new ExpectedException(String.format("Cancel check-in failure -> Cannot find any passengers in Journey[%s]",
					journey.getJourneyId()), new ErrorInfo(ErrorCodeEnum.ERR_CANCEL_CHECK_IN_PASSENGER_NOT_FOUND));
		}
		
		List<CprJourneyPassenger> cprPassengers = journey.getPassengers();
		
		/** build request passenger CprUniqueCustomerIds */
		List<String> requestCprUniqueCustomerIds = new ArrayList<>(); 
		for(String passengerId : requestDTO.getPassengerIds()) {
			if(StringUtils.isEmpty(passengerId)) {
				continue;
			}
			
			CprJourneyPassenger cprJourneyPassenger = getCprJourneyPassengerById(cprPassengers, passengerId);
			if(cprJourneyPassenger == null) {
				throw new ExpectedException(String.format("Cancel check-in failure -> Cannot find passenger[%s] in Journey[%s]",
						passengerId, journey.getJourneyId()), new ErrorInfo(ErrorCodeEnum.ERR_CANCEL_CHECK_IN_PASSENGER_NOT_FOUND));
			}
			
			requestCprUniqueCustomerIds.add(cprJourneyPassenger.getCprUniqueCustomerId());
		}
		
		/** Build cancelCheckInRequestDTO */
		CancelCheckInRequestDTO cancelCheckInRequestDTO = new CancelCheckInRequestDTO();
		cancelCheckInRequestDTO.setJourneyId(requestDTO.getJourneyId());
		cancelCheckInRequestDTO.setUniqueCustomerIds(requestCprUniqueCustomerIds);
		return cancelCheckInRequestDTO;
	}
	
	/**
	 * Get cprJourneyPassenger by passengerId
	 * 
	 * @param cprPassengers
	 * @param passengerId
	 * @return
	 */
	private CprJourneyPassenger getCprJourneyPassengerById(List<CprJourneyPassenger> cprPassengers, String passengerId) {
		if(CollectionUtils.isEmpty(cprPassengers) || StringUtils.isEmpty(passengerId)) {
			return null;
		}
		
		return cprPassengers.stream().filter(passenger -> passenger != null && passengerId.equals(passenger.getPassengerId())).findFirst().orElse(null);
	}

	/**
	 * Build segments for can cancel check-in
	 * 
	 * @param booking
	 * @param cprSegments
	 * @return
	 */
	private List<CanCancelCheckInSegmentDTO> buildCanCancelSegments(Booking booking, List<CprJourneySegment> cprSegments) {
		if(booking == null || CollectionUtils.isEmpty(booking.getSegments()) || CollectionUtils.isEmpty(cprSegments)) {
			return null;
		}
		
		List<CprJourneySegment> canCheckInSegments = cprSegments.stream().filter(CprJourneySegment::getCanCheckIn).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(canCheckInSegments)) {
			logger.warn(String.format("buildCanCancelSegments -> no canCheckIn segments found in cprJourney, booking[%s]", booking.getOneARloc()));
			return null;
		}
		
		List<CanCancelCheckInSegmentDTO> canCancelCheckInSegments = new ArrayList<>();
		for(CprJourneySegment cprSegment : canCheckInSegments) {
			if(cprSegment == null) {
				continue;
			}
			
			CanCancelCheckInSegmentDTO canCancelCheckInSegment = new CanCancelCheckInSegmentDTO();
			canCancelCheckInSegment.setSegmentId(cprSegment.getSegmentId());
			canCancelCheckInSegment.setCheckedIn(cprSegment.getCanCheckIn());
			canCancelCheckInSegment.setCheckInStandBy(checkSegmentStandBy(booking, cprSegment.getSegmentId()));
			
			Segment segment = BookingBuildUtil.getSegmentById(booking.getSegments(), cprSegment.getSegmentId());
			if(segment != null) {
				canCancelCheckInSegment.setOriginPort(segment.getOriginPort());
				canCancelCheckInSegment.setDestPort(segment.getDestPort());
				canCancelCheckInSegment.setDepartureTime(covertDepartureArrivalTime(segment.getDepartureTime()));
				canCancelCheckInSegment.setArrivalTime(covertDepartureArrivalTime(segment.getArrivalTime()));
				canCancelCheckInSegment.setOperateSegmentNumber(segment.getOperateSegmentNumber());
				canCancelCheckInSegment.setMarketSegmentNumber(segment.getMarketSegmentNumber());
				canCancelCheckInSegment.setOperateCompany(segment.getOperateCompany());
				canCancelCheckInSegment.setMarketCompany(segment.getMarketCompany());
				canCancelCheckInSegment.setStatus(Optional.ofNullable(segment.getSegmentStatus()).orElse(new SegmentStatus()).getStatus());
				canCancelCheckInSegment.setAirCraftType(segment.getAirCraftType());
				canCancelCheckInSegment.setFlown(segment.isFlown());
				canCancelCheckInSegment.setCheckedIn(segment.isCheckedIn());
			} else {
				logger.warn(String.format("buildCanCancelSegments -> can't find segment[id: %s] in PNR[%s] booking",
						cprSegment.getSegmentId(), booking.getOneARloc()));
			}
			
			canCancelCheckInSegments.add(canCancelCheckInSegment);
		}
		return canCancelCheckInSegments;
	}
	
	/**
	 * check if the segment is stand by
	 * @param booking
	 * @param segmentId
	 * @return boolean
	 */
	private boolean checkSegmentStandBy(Booking booking, String segmentId) {
		if (booking == null || CollectionUtils.isEmpty(booking.getCprJourneys()) || StringUtils.isEmpty(segmentId)) {
			return false;
		}
		Journey journey = booking.getCprJourneys().stream()
		.filter(j -> !CollectionUtils.isEmpty(j.getSegments()) && j.getSegments().stream().anyMatch(s -> !StringUtils.isEmpty(s.getSegmentId()) && s.getSegmentId().equals(segmentId))).findFirst().orElse(null);
		
		return journey != null && !CollectionUtils.isEmpty(journey.getPassengerSegments()) && journey.getPassengerSegments().stream().anyMatch(ps -> segmentId.equals(ps.getSegmentId()) && ps.isCheckInStandBy());
	}

	/**
	 * Covert DepartureArrivalTime to DepartureArrivalTimeDTO
	 * 
	 * @param departureTime
	 * @return
	 */
	private DepartureArrivalTimeDTO covertDepartureArrivalTime(DepartureArrivalTime departureArrivalTime) {
		if(departureArrivalTime == null) {
			return null;
		}
		
		DepartureArrivalTimeDTO departureArrivalTimeDTO = new DepartureArrivalTimeDTO();
		departureArrivalTimeDTO.setPnrTime(departureArrivalTime.getPnrTime());
		departureArrivalTimeDTO.setRtfsActualTime(departureArrivalTime.getRtfsActualTime());
		departureArrivalTimeDTO.setRtfsEstimatedTime(departureArrivalTime.getRtfsEstimatedTime());
		departureArrivalTimeDTO.setRtfsScheduledTime(departureArrivalTime.getRtfsScheduledTime());
		departureArrivalTimeDTO.setTimeZoneOffset(departureArrivalTime.getTimeZoneOffset());
		
		return departureArrivalTimeDTO;
	}

	/**
	 * Build passengers for can cancel check-in
	 * 
	 * @param booking
	 * @param cprPassengers
	 * @return
	 */
	private List<CanCancelCheckInPassengerDTO> buildCanCancelPassengers(Booking booking, List<CprJourneyPassenger> cprPassengers) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengers()) || CollectionUtils.isEmpty(cprPassengers)) {
			return null;
		}
		
		List<CanCancelCheckInPassengerDTO> canCancelCheckInPassengers = new ArrayList<>();
		for(CprJourneyPassenger cprPassenger : cprPassengers) {
			if(cprPassenger == null) {
				continue;
			}
			
			CanCancelCheckInPassengerDTO canCancelCheckInPassenger = new CanCancelCheckInPassengerDTO();
			canCancelCheckInPassenger.setPassengerId(cprPassenger.getPassengerId());
			canCancelCheckInPassenger.setCprUniqueCustomerId(cprPassenger.getCprUniqueCustomerId());
			canCancelCheckInPassenger.setCheckedIn(cprPassenger.isCheckedIn());
			canCancelCheckInPassenger.setCheckInStandBy(cprPassenger.isCheckInStandBy());
			// both the checked in status and stand by status are available to cancel, so no need to check whether it is checked in
			canCancelCheckInPassenger.setCanCancelCheckIn(cprPassenger.isCanCancelCheckIn());
			canCancelCheckInPassenger.setErrors(cprPassenger.getErrors());
			
			Passenger passenger = BookingBuildUtil.getPassengerById(booking.getPassengers(), cprPassenger.getPassengerId());
			if(passenger != null) {
				canCancelCheckInPassenger.setParentId(passenger.getParentId());
				canCancelCheckInPassenger.setPassengerType(passenger.getPassengerType());
				canCancelCheckInPassenger.setTitle(passenger.getTitle());
				canCancelCheckInPassenger.setFamilyName(passenger.getFamilyName());
				canCancelCheckInPassenger.setGivenName(passenger.getGivenName());
				canCancelCheckInPassenger.setLoginPassenger(BooleanUtils.isTrue(passenger.isPrimaryPassenger()));
			} else {
				logger.warn(String.format("buildCanCancelPassengers -> can't find passenger[id: %s, cprId: %s] in PNR[%s] booking",
						cprPassenger.getPassengerId(), cprPassenger.getCprUniqueCustomerId(), booking.getOneARloc()));
			}
			canCancelCheckInPassengers.add(canCancelCheckInPassenger);
		}
		
		return sortCanCancelCheckInPassengers(canCancelCheckInPassengers);
	}

	/**
	 * Sort passengers for can cancel check-in
	 * rule: 
	 * 1. can cancel check-in passenger -> can cancel check-in
	 * 2. passengerId
	 * 3. login passenger will be the first passenger in 2 list
	 * 4. infant will be put at the end the list(BFF will assign infant to their adult)
	 * 
	 * @param canCancelCheckInPassengers
	 * @return
	 */
	private List<CanCancelCheckInPassengerDTO> sortCanCancelCheckInPassengers(List<CanCancelCheckInPassengerDTO> canCancelCheckInPassengers) {
		if(CollectionUtils.isEmpty(canCancelCheckInPassengers)) {
			return null;
		}
		
		List<CanCancelCheckInPassengerDTO> adults = new ArrayList<>(); // Adults
		List<CanCancelCheckInPassengerDTO> infants = new ArrayList<>(); // Infants without seat
		for(CanCancelCheckInPassengerDTO canCancelCheckInPassenger : canCancelCheckInPassengers) {
			if(canCancelCheckInPassenger == null) {
				continue;
			}
			
			if(StringUtils.isNotEmpty(canCancelCheckInPassenger.getParentId())) {
				infants.add(canCancelCheckInPassenger);
			} else {
				adults.add(canCancelCheckInPassenger);
			}
		}
		
		/**
		 * sorting order: 1. canCancelCheckIn 2.passengerId 3. isLoginPassenger
		 * */
		List<CanCancelCheckInPassengerDTO> sortedAdults = adults.stream()
				.sorted(Comparator.comparing(CanCancelCheckInPassengerDTO::isCanCancelCheckIn, this::compareBoolean))
				.sorted(Comparator.comparing(CanCancelCheckInPassengerDTO::getPassengerId))
				.sorted(Comparator.comparing(CanCancelCheckInPassengerDTO::isLoginPassenger, this::compareBoolean))
				.collect(Collectors.toList());
		
		List<CanCancelCheckInPassengerDTO> passengers = new ArrayList<>();
		passengers.addAll(sortedAdults);
		passengers.addAll(infants);
		return passengers;
	}
	
	/**
	 * compare b1 & b2
	 * used for canCancel/cancel passenger order sorting
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	private int compareBoolean(boolean b1, boolean b2) {
		if(b1 && !b2) {
			return -1;
		} else if( !b1 && b2){
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Get cprJourney from booking by journey id
	 * 
	 * @param booking
	 * @param journeyId
	 * @param rloc
	 * @return
	 */
	private Journey getCprJourneyById(Booking booking, String journeyId, String rloc) {
		if(booking == null || CollectionUtils.isEmpty(booking.getCprJourneys())) {
			logger.info(String.format("getCprJourneyById-> Cannot find booking[%s] by rloc[%s] or can't find any journeys under booking.", booking == null, rloc));
			return null;
		}
		
		return booking.getCprJourneys().stream().filter(journey -> Objects.equals(journey.getJourneyId(), journeyId)).findFirst().orElse(null);
	}

	/**
	 * 	if user had a paid EXL seat before check in and changes the seat to a regular seat while check in, add remark to PNR
	 * @param bookingMergedCpr
	 * @param tempSeat
	 * @throws BusinessBaseException 
	 */
	private void addRemarkForSeat(Booking bookingMergedCpr, List<TempSeat> tempSeats) throws BusinessBaseException {
		if (CollectionUtils.isEmpty(bookingMergedCpr.getPassengerSegments()) || CollectionUtils.isEmpty(tempSeats)) {
			return;
		}
		
		//remark info
		List<RemarkInfo> remarkInfos = new ArrayList<>();
		for (TempSeat tempSeat : tempSeats) {
			if (StringUtils.isEmpty(tempSeat.getPassengerId()) || StringUtils.isEmpty(tempSeat.getSegmentId())) {
				continue;
			}
			
			// get the passenger segment before check in
			PassengerSegment originalPassengerSegment = getPassengerSegmentByIds(bookingMergedCpr.getPassengerSegments(), tempSeat.getPassengerId(), tempSeat.getSegmentId());
			
			SeatDetail originalSeat = originalPassengerSegment == null ? null : originalPassengerSegment.getOriginalSeat();
			if (originalSeat != null && originalSeat.isExlSeat() && !tempSeat.isExlSeat()) {
				RemarkInfo remarkInfo = new RemarkInfo();
				remarkInfo.setPassengerId(tempSeat.getPassengerId());
				remarkInfo.setSegmentId(tempSeat.getSegmentId());
				remarkInfos.add(remarkInfo);
			}
		}
		updateSeatServiceImplV2.addRemark(remarkInfos, bookingMergedCpr.getOneARloc());
	}

	/**
	 * get passenger segment by ids
	 * @param passengerSegments
	 * @param passengerId
	 * @param segmentId
	 * @return
	 */
	private PassengerSegment getPassengerSegmentByIds(List<PassengerSegment> passengerSegments, String passengerId,
			String segmentId) {
		return passengerSegments.stream().filter(ps -> passengerId.equals(ps.getPassengerId()) && segmentId.equals(ps.getSegmentId())).findFirst().orElse(null);
	}

	/**
	 * Get list of errorInfos with parameter - field name
	 * 
	 * @param checkInErrorInfos
	 * @param fieldNames
	 * @return
	 */
	private List<ErrorInfo> getErrorInfosByFieldName(List<ErrorInfo> checkInErrorInfos, String... fieldNames) {
		if(CollectionUtils.isEmpty(checkInErrorInfos) || fieldNames == null || fieldNames.length == 0) {
			return checkInErrorInfos;
		}
		
		List<String> fieldNameList = Arrays.asList(fieldNames);
		return checkInErrorInfos.stream().filter(error -> error != null && fieldNameList.contains(error.getFieldName())).collect(Collectors.toList());
	}

	/**
	 * Check retrievePnrBooking is valid:  
	 * 1. not null
	 * 2. name identification
	 * 
	 * @param retrievePnrBooking
	 * @param rloc
	 * @param loginInfo
	 * @throws BusinessBaseException
	 */
	private void checkRetrievePnrBooking(RetrievePnrBooking retrievePnrBooking, String rloc, LoginInfo loginInfo) throws BusinessBaseException {
		if (retrievePnrBooking == null) {
			throw new ExpectedException(String.format("Cannot find booking by rloc:%s", rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_NOFOUNDBOOKING_NONMEMBER_LOGIN));
		}
		
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
	}
	
	@SuppressWarnings("unchecked")
	private List<TempSeat> getSeatsByToken(String rloc) {
		return mbTokenCacheLockRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.TEMP_SEAT, TokenLockKeyEnum.MMB_TEMP_SEAT, rloc, ArrayList.class);
	}

	/**
	 * Allocate seat before accept check in
	 * @param retrieveBooking
	 * @param requestDTO
	 * @param tempSeat
	 * @return
	 * @throws BusinessBaseException
	 */
	private AllocateSeatResponseDTO assignSeat(Booking retrieveBooking, CheckInAcceptRequestDTO requestDTO, List<TempSeat> tempSeat){
		Journey retrieveJourney = retrieveBooking.getCprJourneys().stream().filter(journey -> journey.getJourneyId().equals(requestDTO.getJourneyId())).findFirst().orElse(null);
		if (tempSeat == null || tempSeat.isEmpty()) {
			logger.warn(String.format("Cannot find temp seat in token level cache by rloc: %s, do not allocate seat", requestDTO.getRloc()));
			return null;
		}
		
		try {			
			AllocateSeatRequestDTO request = buildAllocateSeatRequest(retrieveJourney, requestDTO, tempSeat);
			return updateSeatServiceImplV2.allocateSeat(request, requestDTO.getRloc());
		} catch (Exception e) {
			logger.error(String.format("Allocate seat throw exception in rloc: %s, continue doing the accept check in", requestDTO.getRloc()));
			return null;
		}
		
	}
	
	/**
	 * Build request for allocate seat
	 * @param retrieveJourney
	 * @param requestDTO
	 * @param tempSeat
	 * @return
	 * @throws ExpectedException
	 */
	private AllocateSeatRequestDTO buildAllocateSeatRequest(Journey retrieveJourney, CheckInAcceptRequestDTO requestDTO, List<TempSeat> tempSeat){
		// Build request
		AllocateSeatRequestDTO request = new AllocateSeatRequestDTO();
		List<PassengerInfoDTO> passengerInfoDTOs = new ArrayList<>();
		request.setJourneyId(requestDTO.getJourneyId());
		request.setPassengers(passengerInfoDTOs);
		
		if (CollectionUtils.isEmpty(requestDTO.getPassengerIds())) {
			return request;
		}
		for(String passengerId: requestDTO.getPassengerIds()) {
			// Find temp seats for this passenger in this journey
			List<TempSeat> paxSeat = tempSeat.stream().filter(seat -> seat.getPassengerId().equals(passengerId) 
					&& seat.getJourneyId().equals(requestDTO.getJourneyId())).collect(Collectors.toList());				
			List<CprJourneyPassengerSegment> cprPaxSegment = retrieveJourney.getPassengerSegments().stream().filter(pax -> pax.getPassengerId().equals(passengerId)).collect(Collectors.toList());
			
			if (CollectionUtils.isEmpty(paxSeat) && CollectionUtils.isEmpty(cprPaxSegment)) {					
				continue;
			}
			
			for(TempSeat seat: paxSeat) {
				// Find passenger segment for this seat
				CprJourneyPassengerSegment paxSegment = cprPaxSegment.stream().filter(paxSeg -> paxSeg.getSegmentId().equals(seat.getSegmentId())).findFirst().orElse(null);
				if (paxSegment != null) {
					buildPassengerInfo(passengerInfoDTOs, cprPaxSegment, seat, paxSegment);
				}
			}
			
		}
		return request;
	}

	/**
	 * Build passenger info for allocate seat request
	 * @param passengerInfoDTOs
	 * @param cprPaxSegment
	 * @param seat
	 * @param paxSegment
	 */
	public void buildPassengerInfo(List<PassengerInfoDTO> passengerInfoDTOs,
			List<CprJourneyPassengerSegment> cprPaxSegment, TempSeat seat, CprJourneyPassengerSegment paxSegment) {
		PassengerInfoDTO passengerInfoDTO = new PassengerInfoDTO();
		List<FlightInfoDTO> flightInfoDTOs = new ArrayList<>();
		passengerInfoDTO.setUniqueCustomerId(cprPaxSegment.get(0).getCprUniqueCustomerId());
		passengerInfoDTO.setFlights(flightInfoDTOs);
		
		FlightInfoDTO flightInfo = new FlightInfoDTO();
		flightInfo.setProductIdentifierDID(paxSegment.getCprProductIdentifierDID());
		flightInfo.setProductIdentifierJID(paxSegment.getCprProductIdentifierJID());
		flightInfo.setSeatNum(seat.getSeatNo());
		if (!StringUtils.isEmpty(seat.getSeatNo())) {
			flightInfo.setExtraLegRoomSeat(seat.isExlSeat());
		}
		flightInfo.setSeatPreference(seat.getSeatPreference());

		flightInfoDTOs.add(flightInfo);
		passengerInfoDTOs.add(passengerInfoDTO);
	}
	
	/**
	 * Get journey by journey id.
	 * Checking booking is not null, journey is in the booking
	 * 
	 * @param booking
	 * @param journeyId
	 * @return
	 * @throws BusinessBaseException
	 */
	private Journey getAndCheckCprJourneyById(Booking booking, String journeyId) throws BusinessBaseException {
		if(booking == null || CollectionUtils.isEmpty(booking.getCprJourneys())) {
			throw new ExpectedException(String.format("CheckIn acceptance failure -> Cannot find booking[%s] or can't find any journeys under booking.",
					booking != null ? booking.getOneARloc() : null), new ErrorInfo(ErrorCodeEnum.ERR_CHECK_IN_ACCEPTANCE_CPRJOURNEY_NO_FOUND));
		}
		
		/** Check request journey in the booking, and this journey is valid. */
		Journey cprJourney = booking.getCprJourneys().stream().filter(journey -> journeyId.equals(journey.getJourneyId())).findFirst().orElse(null);
		if(cprJourney == null || CollectionUtils.isEmpty(cprJourney.getPassengerSegments())) {
			throw new ExpectedException(String.format("CheckIn acceptance failure -> Cannot find request Journey[%s] in booking[%s] or no passengerSegments in this journey.",
					journeyId, booking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_CHECK_IN_ACCEPTANCE_REQUEST_JOURNEY_NO_FOUND));
		}
		return cprJourney;
	}
	
	/**
	 * Build check-in acceptance request
	 * 
	 * @param requestDTO
	 * @param cprJourney
	 * @return
	 * @throws BusinessBaseException
	 */
	private CheckInRequestDTO buildCheckInAcceptRequest(CheckInAcceptRequestDTO requestDTO, Journey cprJourney) throws BusinessBaseException {
		/** build passenger info */
		List<PassengerInfoDTO> passengerInfoDTOs = new ArrayList<>(); 
		for(String passengerId : requestDTO.getPassengerIds()) {
			if(StringUtils.isEmpty(passengerId)) {
				continue;
			}
			
			List<CprJourneyPassengerSegment> cprPassengerSegments = cprJourney.getPassengerSegments().stream().filter(ps -> passengerId.equals(ps.getPassengerId())).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(cprPassengerSegments)) {
				throw new ExpectedException(String.format("CheckIn acceptance failure -> Cannot find request passenger[%s] under Journey[%s] in booking[%s].",
						passengerId, requestDTO.getJourneyId() ,requestDTO.getRloc()), new ErrorInfo(ErrorCodeEnum.ERR_CHECK_IN_ACCEPTANCE_REQUEST_PASSENGER_NO_FOUND));
			}
			
			PassengerInfoDTO passengerInfoDTO = buildCheckInAcceptPassengerInfoDTO(cprPassengerSegments);
			if(passengerInfoDTO == null) {
				throw new ExpectedException(String.format("CheckIn acceptance failure -> Cannot find any flights for request passenger[%s] under Journey[%s] in booking[%s].",
						passengerId, requestDTO.getJourneyId() ,requestDTO.getRloc()), new ErrorInfo(ErrorCodeEnum.ERR_CHECK_IN_ACCEPTANCE_VALID_SEGMENT_NO_FOUND));
			}
			
			passengerInfoDTOs.add(passengerInfoDTO);
		}
		
		if(CollectionUtils.isEmpty(passengerInfoDTOs)) {
			throw new ExpectedException(String.format("CheckIn acceptance failure -> Cannot find any flights for all request passengers under Journey[%s] in booking[%s].",
					requestDTO.getJourneyId() ,requestDTO.getRloc()), new ErrorInfo(ErrorCodeEnum.ERR_CHECK_IN_ACCEPTANCE_VALID_SEGMENT_NO_FOUND));
		}
		
		/** Build CheckInRequestDTO */
		CheckInRequestDTO checkInRequestDTO = new CheckInRequestDTO();
		checkInRequestDTO.setJourneyId(requestDTO.getJourneyId());
		checkInRequestDTO.setPassengers(passengerInfoDTOs);
		
		return checkInRequestDTO;
	}

	/**
	 * Build PassengerInfoDTO for requesting check-in accept
	 * 
	 * @param cprPassengerSegments
	 * @return
	 */
	private PassengerInfoDTO buildCheckInAcceptPassengerInfoDTO(List<CprJourneyPassengerSegment> cprPassengerSegments) {
		List<FlightInfoDTO> flightInfoDTOs = new ArrayList<>();
		for(CprJourneyPassengerSegment cprPassengerSegment : cprPassengerSegments) {
			if(cprPassengerSegment == null || BooleanUtils.isNotTrue(cprPassengerSegment.getCanCheckIn()) 
					|| BooleanUtils.isTrue(cprPassengerSegment.getCheckedIn())) {
				logger.info(String.format("CheckIn acceptance -> skip segment[DID:%s] due to this segment can't check-in",
						cprPassengerSegment != null ? cprPassengerSegment.getCprProductIdentifierDID() : null));
				continue;
			}
			
			FlightInfoDTO flightInfoDTO = new FlightInfoDTO();
			flightInfoDTO.setProductIdentifierDID(cprPassengerSegment.getCprProductIdentifierDID());
			flightInfoDTO.setProductIdentifierJID(cprPassengerSegment.getCprProductIdentifierJID());
			flightInfoDTOs.add(flightInfoDTO);
		}
		
		if(CollectionUtils.isEmpty(flightInfoDTOs)) {
			return null;
		}
		
		PassengerInfoDTO passengerInfoDTO = new PassengerInfoDTO();
		String uniqueCustomerId = cprPassengerSegments.stream().filter(ps -> ps != null && StringUtils.isNotEmpty(ps.getCprUniqueCustomerId()))
				.map(CprJourneyPassengerSegment::getCprUniqueCustomerId).findFirst().orElse(null);
		passengerInfoDTO.setUniqueCustomerId(uniqueCustomerId);
		passengerInfoDTO.setFlights(flightInfoDTOs);
		
		return passengerInfoDTO;
	}
	
	/**
	 * Accept check-in through OLCI client
	 * 
	 * @param checkInRequestDTO
	 * @param rloc
	 * @return
	 * @throws ExpectedException 
	 */
	private CheckInResponseDTO acceptCheckIn(CheckInRequestDTO checkInRequestDTO, String rloc) {
		try {
			ResponseEntity<CheckInResponseDTO> responseEntity = olciClient.acceptCheckIn(checkInRequestDTO, rloc, null);
			return responseEntity.getBody();
		} catch(Exception e) {
			logger.error(String.format("Journey[%s] check-in failure when trying to call OLCI",
					checkInRequestDTO != null ? checkInRequestDTO.getJourneyId() : null), e);
			
			// handle socket timed out exception(read time out)
			if(e.getCause() != null && e.getCause() instanceof SocketTimeoutException) {
				CheckInResponseDTO checkInResponseDTO = new CheckInResponseDTO();
				com.cathaypacific.olciconsumer.model.response.ErrorInfo errorInfo = new com.cathaypacific.olciconsumer.model.response.ErrorInfo();
				errorInfo.setErrorCode(ErrorCodeEnum.ERR_HTTP_READ_TIME_OUT.getCode());
				errorInfo.setFieldName(MMBBizruleConstants.ERROR_FIELD_NAME_READ_TIME_OUT);
				errorInfo.setType(ErrorCodeEnum.ERR_HTTP_READ_TIME_OUT.getType().getType());
				checkInResponseDTO.addError(errorInfo);
				return checkInResponseDTO;
			}
		}		
		return null;
	}
	
	/**
	 * Covert CPR ErrorInfos to MMB ErrorInfo by cprErrorType,
	 * if parameter "cprErroType" is empty, return all erorrInfos.
	 * 
	 * @param responseDTO
	 * @param cprErrorType
	 * @return
	 */
	private List<ErrorInfo> getAndCovertErrorInfosByType(BaseResponseDTO responseDTO, String cprErrorType) {
		if(responseDTO == null) {
			return new ArrayList<>();
		}
		
		List<com.cathaypacific.olciconsumer.model.response.ErrorInfo> cprErrors = responseDTO.getErrors();
		if(StringUtils.isNotEmpty(cprErrorType) && CollectionUtils.isNotEmpty(cprErrors)) {
			cprErrors = cprErrors.stream().filter(error -> error != null && cprErrorType.equals(error.getType())).collect(Collectors.toList());
		}
		
		return PnrCprMergeHelper.covertErrorInfos(cprErrors);
	}
	
	/**
	 * Covert JourneyDTO to CheckInAcceptCprJourneyDTO
	 * 
	 * @param cprJourney 
	 * @param journeyDTO
	 * @param bookingMergedCpr 
	 * @return
	 */
	private CheckInAcceptCprJourneyDTO buildCheckInAcceptCprJourney(Journey cprJourney, JourneyDTO journeyDTO, Booking bookingMergedCpr) {
		if(journeyDTO == null) {
			return generateCheckInFailureJourneyDTO(cprJourney);
		}
		
		CheckInAcceptCprJourneyDTO checkInAcceptCprJourneyDTO = new CheckInAcceptCprJourneyDTO();
		checkInAcceptCprJourneyDTO.setJourneyId(journeyDTO.getJourneyId());
		
		/** if any errorType = "S", return this journey with these errors */
		List<com.cathaypacific.olciconsumer.model.response.ErrorInfo> cprStopErrorInfos = Optional.ofNullable(journeyDTO.getErrors()).orElse(new ArrayList<>()).stream()
				.filter(error -> error != null && OLCIConstants.CPR_ERROR_INFO_TYPE_S.equals(error.getType()))
				.collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(cprStopErrorInfos)) {
			checkInAcceptCprJourneyDTO.setErrors(PnrCprMergeHelper.covertErrorInfos(cprStopErrorInfos));
			return checkInAcceptCprJourneyDTO;
		}
		
		checkInAcceptCprJourneyDTO.setErrors(PnrCprMergeHelper.covertErrorInfos(journeyDTO.getErrors()));
		checkInAcceptCprJourneyDTO.setPassengers(buildCheckInAcceptPassengers(journeyDTO, cprJourney.getPassengers()));
		checkInAcceptCprJourneyDTO.setSegments(buildCheckInAcceptCprSegments(journeyDTO, cprJourney.getSegments(), cprJourney.getPassengerSegments()));
		
		List<PassengerSegment> passengerSegments = bookingMergedCpr == null
				|| CollectionUtils.isEmpty(bookingMergedCpr.getPassengerSegments()) ? null
						: bookingMergedCpr.getPassengerSegments();
		checkInAcceptCprJourneyDTO.setPassengerSegments(buildCheckInAcceptPassengerSegments(journeyDTO, cprJourney.getPassengerSegments(), passengerSegments));
		checkInAcceptCprJourneyDTO.setInhibitUSBP(journeyDTO.getInhibitUSBP());
		checkInAcceptCprJourneyDTO.setAllowMBP(journeyDTO.getAllowMBP());
		
		return checkInAcceptCprJourneyDTO;
	}
	
	/**
	 * Build CheckInAcceptPassengerDTO list
	 * 
	 * @param journeyDTO
	 * @param passengers
	 * @return
	 */
	private List<CheckInAcceptPassengerDTO> buildCheckInAcceptPassengers(JourneyDTO journeyDTO, List<CprJourneyPassenger> passengers) {
		if(journeyDTO == null || CollectionUtils.isEmpty(journeyDTO.getPassengers())
				||CollectionUtils.isEmpty(passengers)) {
			return null;
		}
		
		List<CheckInAcceptPassengerDTO> checkInAcceptPassengerDTOs = new ArrayList<>();
		for(CprJourneyPassenger passenger : passengers) {
			if(passenger == null) {
				continue;
			}
			
			CheckInAcceptPassengerDTO checkInAcceptPassengerDTO = new CheckInAcceptPassengerDTO();
			checkInAcceptPassengerDTO.setPassengerId(passenger.getPassengerId());
			checkInAcceptPassengerDTO.setCprUniqueCustomerId(passenger.getCprUniqueCustomerId());
			
			
			PassengerDTO cprPassenger = pnrCprMergeHelper.getCprPassengerByCprUniqueCustomerId(journeyDTO.getPassengers(), passenger.getCprUniqueCustomerId());
			if(cprPassenger != null) {
				checkInAcceptPassengerDTO.addAllErrors(PnrCprMergeHelper.covertErrorInfos(cprPassenger.getErrors()));
				checkInAcceptPassengerDTO.setCheckedIn(cprPassenger.isCheckInAccepted());
				checkInAcceptPassengerDTO.setCheckInStandBy(cprPassenger.isStandBy());
				checkInAcceptPassengerDTO.setInhibitBP(cprPassenger.isInhibitBP());
			} else {
				logger.warn(String.format("CheckIn acceptance -> can't find cprPassenger[id:%s, CprId:%s] in accept check-in response!", passenger.getPassengerId(), passenger.getCprUniqueCustomerId()));
			}
			checkInAcceptPassengerDTOs.add(checkInAcceptPassengerDTO);
		}
			
		return checkInAcceptPassengerDTOs;
	}
	
	/**
	 * Build CheckInAcceptCprSegmentDTO list
	 * 
	 * @param journeyDTO
	 * @param segments
	 * @param list 
	 * @return
	 */
	private List<CheckInAcceptCprSegmentDTO> buildCheckInAcceptCprSegments(JourneyDTO journeyDTO,
			List<CprJourneySegment> segments, List<CprJourneyPassengerSegment> passengerSegments) {
		if(journeyDTO == null || CollectionUtils.isEmpty(journeyDTO.getPassengers())
				|| CollectionUtils.isEmpty(segments)
				|| CollectionUtils.isEmpty(passengerSegments)) {
			return null;
		}
		
		List<CheckInAcceptCprSegmentDTO> checkInAcceptCprSegmentDTOs = new ArrayList<>();
		
		for(CprJourneySegment segment : segments) {
			if(segment == null) {
				continue;
			}
			
			List<CprJourneyPassengerSegment> passengerSegmentsWithSameSegmentId = passengerSegments.stream().filter(ps -> ps != null && Objects.equals(ps.getSegmentId(), segment.getSegmentId())).collect(Collectors.toList());
			
			List<FlightDTO> cprSegments = journeyDTO.getPassengers().stream().map(PassengerDTO::getFlights).flatMap(Collection::stream)
					.filter(flight -> flight != null && containsCprSegmentIds(passengerSegmentsWithSameSegmentId, flight.getProductIdentifierDID(), flight.getProductIdentifierJID()))
					.collect(Collectors.toList());
			
			CheckInAcceptCprSegmentDTO checkInAcceptCprSegmentDTO = new CheckInAcceptCprSegmentDTO();
			checkInAcceptCprSegmentDTO.setSegmentId(segment.getSegmentId());
			
			List<com.cathaypacific.olciconsumer.model.response.ErrorInfo> cprSegmentErrors = cprSegments.stream()
					.filter(flight -> flight != null && CollectionUtils.isNotEmpty(flight.getErrors()))
					.map(FlightDTO::getErrors).flatMap(Collection::stream).collect(Collectors.toList());
			checkInAcceptCprSegmentDTO.addAllErrors(PnrCprMergeHelper.covertErrorInfos(cprSegmentErrors));
			
			checkInAcceptCprSegmentDTOs.add(checkInAcceptCprSegmentDTO);
		}
		
		return checkInAcceptCprSegmentDTOs;
	}

	/**
	 * DID & JID is in list of CprJourneyPassengerSegment or not.
	 * 
	 * @param cprPassengerSegments
	 * @param productIdentifierDID
	 * @param productIdentifierJID
	 * @return
	 */
	private boolean containsCprSegmentIds(List<CprJourneyPassengerSegment> cprPassengerSegments,
			String productIdentifierDID, String productIdentifierJID) {
		return cprPassengerSegments.stream().anyMatch(ps -> ps != null 
				&& Objects.equals(ps.getCprProductIdentifierDID(), productIdentifierDID) 
				&& Objects.equals(ps.getCprProductIdentifierJID(), productIdentifierJID));
	}

	/**
	 * Build CheckInAcceptPassengerSegmentDTO list
	 * 
	 * @param journeyDTO
	 * @param cprPassengerSegments
	 * @param passengerSegments 
	 * @return List<CheckInAcceptPassengerSegmentDTO>
	 */
	private List<CheckInAcceptPassengerSegmentDTO> buildCheckInAcceptPassengerSegments(JourneyDTO journeyDTO, List<CprJourneyPassengerSegment> cprPassengerSegments, List<PassengerSegment> passengerSegments) {
		if(journeyDTO == null || CollectionUtils.isEmpty(journeyDTO.getPassengers())
				|| CollectionUtils.isEmpty(cprPassengerSegments)) {
			return null;
		}
		
		List<CheckInAcceptPassengerSegmentDTO> checkInAcceptPassengerSegmentDTOs = new ArrayList<>();
		
		for(CprJourneyPassengerSegment cprPassengerSegment : cprPassengerSegments) {
			if(cprPassengerSegment == null 
					|| StringUtils.isEmpty(cprPassengerSegment.getCprUniqueCustomerId())
					|| (StringUtils.isEmpty(cprPassengerSegment.getCprProductIdentifierJID()) 
							&& StringUtils.isEmpty(cprPassengerSegment.getCprProductIdentifierDID()))) {
				continue;
			}
			
			PassengerDTO cprPassenger = pnrCprMergeHelper.getCprPassengerByCprUniqueCustomerId(journeyDTO.getPassengers(), cprPassengerSegment.getCprUniqueCustomerId());
			FlightDTO cprSegment = pnrCprMergeHelper.getCprSegmentByIds(cprPassenger, cprPassengerSegment.getCprProductIdentifierDID(), cprPassengerSegment.getCprProductIdentifierJID());
			
			if(cprPassenger != null && cprSegment != null) {
				CheckInAcceptPassengerSegmentDTO checkInAcceptPassengerSegmentDTO = new CheckInAcceptPassengerSegmentDTO();
				checkInAcceptPassengerSegmentDTO.addAllErrors(PnrCprMergeHelper.covertErrorInfos(cprSegment.getErrors()));
				
				/** Passenger */ 
				checkInAcceptPassengerSegmentDTO.setPassengerId(cprPassengerSegment.getPassengerId());
				checkInAcceptPassengerSegmentDTO.setCprUniqueCustomerId(cprPassengerSegment.getCprUniqueCustomerId());
				
				/** Segment */
				checkInAcceptPassengerSegmentDTO.setSegmentId(cprPassengerSegment.getSegmentId());
				checkInAcceptPassengerSegmentDTO.setCprProductIdentifierDID(cprPassengerSegment.getCprProductIdentifierDID());
				checkInAcceptPassengerSegmentDTO.setCprProductIdentifierJID(cprPassengerSegment.getCprProductIdentifierJID());
				
				/** Check-in flag */
				checkInAcceptPassengerSegmentDTO.setCheckedIn(cprSegment.isCheckInAccepted());
				
				/** stand by flag */
				checkInAcceptPassengerSegmentDTO.setCheckInStandBy(cprSegment.isStandBy());
				checkInAcceptPassengerSegmentDTO.setSecurityNumber(cprSegment.getSecurityNumber());
				
				/** Seat number after check-in if exist. */
				checkInAcceptPassengerSegmentDTO.setSeatNumber(Optional.ofNullable(cprSegment.getSeat()).map(SeatDTO::getSeatNum).orElse(null));
				checkInAcceptPassengerSegmentDTO.setSeatAutoAssigned(checkSeatAutoAssigned(passengerSegments,
						cprPassengerSegment, checkInAcceptPassengerSegmentDTO));
				
				checkInAcceptPassengerSegmentDTOs.add(checkInAcceptPassengerSegmentDTO);
			}
		}
		
		return checkInAcceptPassengerSegmentDTOs;
	}

	/**
	 * check if the seat is auto assigned
	 * 		if there's no seat before the acceptance but there's one after it, then the seat is auto assigned
	 * @param passengerSegments
	 * @param cprPassengerSegment
	 * @param checkInAcceptPassengerSegmentDTO
	 * @return
	 */
	private boolean checkSeatAutoAssigned(List<PassengerSegment> passengerSegments, CprJourneyPassengerSegment cprPassengerSegment,
			CheckInAcceptPassengerSegmentDTO checkInAcceptPassengerSegmentDTO) {
		// get the passengerSegment before acceptance
		PassengerSegment passengerSegment = CollectionUtils.isEmpty(passengerSegments) ? null
				: getPassengerSegmentByIds(passengerSegments, cprPassengerSegment.getPassengerId(),
						cprPassengerSegment.getSegmentId());
		return !StringUtils.isEmpty(checkInAcceptPassengerSegmentDTO.getSeatNumber()) && (passengerSegment == null
				|| passengerSegment.getSeat() == null || StringUtils.isEmpty(passengerSegment.getSeat().getSeatNo()));
	}
	
	/**
	 * Add original seat to response and combine error info
	 * @param checkInAcceptResponse
	 * @param allocateSeatResponseDTO
	 * @param tempSeat
	 * @param bookingMergedCpr
	 * @param cprJourney
	 */
	private void combineAllocateSeatResponse(CheckInAcceptResponseDTO checkInAcceptResponse, AllocateSeatResponseDTO allocateSeatResponseDTO, List<TempSeat> tempSeat, Booking bookingMergedCpr, Journey cprJourney) {
		addOriginalSeat(checkInAcceptResponse, tempSeat, bookingMergedCpr);
		mergeAllocateSeatErrorToResponse(checkInAcceptResponse, allocateSeatResponseDTO, cprJourney);
	}
	
	/**
	 * Merge allocate seat response error or warning to response
	 * @param checkInAcceptResponse
	 * @param allocateSeatResponseDTO
	 * @param cprJourney
	 */
	private void mergeAllocateSeatErrorToResponse(CheckInAcceptResponseDTO checkInAcceptResponse, AllocateSeatResponseDTO allocateSeatResponseDTO, Journey cprJourney) {
		if (checkInAcceptResponse == null || allocateSeatResponseDTO == null) {
			return;
		}
		CheckInAcceptCprJourneyDTO acceptJourney = checkInAcceptResponse.getCprJourney();
		JourneyDTO allocateSeatJourney = allocateSeatResponseDTO.getJourney();
		if (acceptJourney != null &&  allocateSeatJourney != null 
				&& acceptJourney.getJourneyId().equals(allocateSeatJourney.getJourneyId())) {
			
			acceptJourney.addAllErrors(PnrCprMergeHelper.covertErrorInfos(allocateSeatJourney.getErrors()));
			mergeAllocateSeatPassengerError(acceptJourney.getPassengers(), allocateSeatJourney.getPassengers());
			mergeAllocateSeatSegmentError(acceptJourney.getSegments(), allocateSeatJourney.getPassengers(), cprJourney.getPassengerSegments());
			mergeAllocateSeatPassengerSegmentError(acceptJourney.getPassengerSegments(), allocateSeatJourney.getPassengers());
		}
	}
	
	/**
	 * Merge allocate seat passenger level error
	 * @param acceptPassengers
	 * @param allocateSeatPassengers
	 */
	private void mergeAllocateSeatPassengerError(List<CheckInAcceptPassengerDTO> acceptPassengers, List<PassengerDTO> allocateSeatPassengers) {
		if (CollectionUtils.isEmpty(acceptPassengers) || CollectionUtils.isEmpty(allocateSeatPassengers)) {
			return;
		}
		
		for(CheckInAcceptPassengerDTO acceptPassenger: acceptPassengers) {
			PassengerDTO allocatePassenger = allocateSeatPassengers.stream().filter(passenger -> acceptPassenger.getCprUniqueCustomerId().equals(passenger.getUniqueCustomerId())).findFirst().orElse(null);
			if (allocatePassenger != null && allocatePassenger.getErrors() != null) {
				acceptPassenger.addAllErrors(PnrCprMergeHelper.covertErrorInfos(allocatePassenger.getErrors()));
			}
		}
	}
	
	/**
	 * Merge allocate seat segment level error
	 * @param acceptSegments
	 * @param allocateSeatPassengers
	 * @param cprPassengerSegments
	 */
	private void mergeAllocateSeatSegmentError(List<CheckInAcceptCprSegmentDTO> acceptSegments, List<PassengerDTO> allocateSeatPassengers, List<CprJourneyPassengerSegment> cprPassengerSegments) {
		if (CollectionUtils.isEmpty(acceptSegments) || CollectionUtils.isEmpty(allocateSeatPassengers) || CollectionUtils.isEmpty(cprPassengerSegments)) {
			return;
		}
		
		for(CheckInAcceptCprSegmentDTO acceptSegment: acceptSegments) {
			// Find passenger segment to get JID, DID
			CprJourneyPassengerSegment cprPaxSeg = cprPassengerSegments.stream().filter(segment -> segment.getSegmentId().equals(acceptSegment.getSegmentId())).findFirst().orElse(null);
			if (cprPaxSeg != null) {
				// Find segment for all passengers
				List<FlightDTO> cprSegments = allocateSeatPassengers.stream().map(PassengerDTO::getFlights).flatMap(Collection::stream)
						.filter(flight -> flight != null && Objects.equals(flight.getProductIdentifierDID(), cprPaxSeg.getCprProductIdentifierDID()) 
						&& Objects.equals(flight.getProductIdentifierJID(), flight.getProductIdentifierJID())).collect(Collectors.toList());
				
				// Find all of the errors
				List<com.cathaypacific.olciconsumer.model.response.ErrorInfo> cprSegmentErrors = cprSegments.stream()
						.filter(flight -> flight != null && CollectionUtils.isNotEmpty(flight.getErrors()))
						.map(FlightDTO::getErrors).flatMap(Collection::stream).collect(Collectors.toList());
				acceptSegment.addAllErrors(PnrCprMergeHelper.covertErrorInfos(cprSegmentErrors));
			}
		}
	}
	
	/**
	 * Merge allocate seat passenger segment level error
	 * @param acceptPassengerSegments
	 * @param allocateSeatPassengers
	 */
	private void mergeAllocateSeatPassengerSegmentError(List<CheckInAcceptPassengerSegmentDTO> acceptPassengerSegments, List<PassengerDTO> allocateSeatPassengers) {
		if (CollectionUtils.isEmpty(acceptPassengerSegments) || CollectionUtils.isEmpty(allocateSeatPassengers)) {
			return;
		}
		
		for(CheckInAcceptPassengerSegmentDTO acceptPassengerSegment: acceptPassengerSegments) {
			FlightDTO acceptPaxSegments = allocateSeatPassengers.stream().filter(passenger -> acceptPassengerSegment.getCprUniqueCustomerId().equals(passenger.getUniqueCustomerId()))
					.map(PassengerDTO::getFlights).flatMap(Collection::stream)
					.filter(flight -> flight != null 
							&& (Objects.equals(flight.getProductIdentifierDID(), acceptPassengerSegment.getCprProductIdentifierDID()) 
							|| Objects.equals(flight.getProductIdentifierJID(), acceptPassengerSegment.getCprProductIdentifierJID())))
					.findFirst().orElse(null);
			if (acceptPaxSegments != null && acceptPaxSegments.getErrors() != null) {
				acceptPassengerSegment.setErrors(PnrCprMergeHelper.covertErrorInfos(acceptPaxSegments.getErrors()));
			}
		}
	}
	

	/**
	 * Add back original seat
	 * @param checkInAcceptResponse
	 * @param tempSeat
	 * @param bookingMergedCpr
	 */
	private void addOriginalSeat(CheckInAcceptResponseDTO checkInAcceptResponse, List<TempSeat> tempSeat, Booking bookingMergedCpr) {
		if (checkInAcceptResponse.getCprJourney() == null) {
			logger.warn(String.format("Cannot find cprJourney in accept checkin response by rloc: %s, do not set original seat", bookingMergedCpr.getOneARloc()));
			return;
		}
		
		if (CollectionUtils.isEmpty(tempSeat)) {
			// If do not have seat in redis cache, find seat number in booking.
			findOriginalSeatInBooking(checkInAcceptResponse, bookingMergedCpr);
		} else {
			// If have temp saved seat in redis, add back original seat number
			findOriginalSeatInRedis(checkInAcceptResponse, tempSeat);
		}
	}

	/**
	 * Find Original Seat In redis
	 * 
	 * @param checkInAcceptResponse
	 * @param tempSeat
	 */
	private void findOriginalSeatInRedis(CheckInAcceptResponseDTO checkInAcceptResponse, List<TempSeat> tempSeat) {
		if(checkInAcceptResponse == null || checkInAcceptResponse.getCprJourney() == null 
				|| CollectionUtils.isEmpty(checkInAcceptResponse.getCprJourney().getPassengerSegments())) {
			return;
		}
		
		for(CheckInAcceptPassengerSegmentDTO cprPassengerSegment : checkInAcceptResponse.getCprJourney().getPassengerSegments()) {
			if(cprPassengerSegment == null) {
				continue;
			}
			
			TempSeat requestSeat = tempSeat.stream().filter(seat -> seat != null 
					&& Objects.equals(seat.getPassengerId(), cprPassengerSegment.getPassengerId())
					&& Objects.equals(seat.getSegmentId(), cprPassengerSegment.getSegmentId())).findFirst().orElse(null);
			 if (requestSeat != null) {
				cprPassengerSegment.setOriginalSeatNumber(requestSeat.getSeatNo());
			}
		}
	}

	/**
	 * Find Original Seat In Booking
	 * 
	 * @param checkInAcceptResponse
	 * @param bookingMergedCpr
	 */
	private void findOriginalSeatInBooking(CheckInAcceptResponseDTO checkInAcceptResponse, Booking bookingMergedCpr) {
		if(bookingMergedCpr == null || CollectionUtils.isEmpty(bookingMergedCpr.getPassengerSegments()) 
				|| checkInAcceptResponse == null || checkInAcceptResponse.getCprJourney() == null
				|| CollectionUtils.isEmpty(checkInAcceptResponse.getCprJourney().getPassengerSegments())) {
			return;
		}
		
		List<PassengerSegment> passengerSegment = bookingMergedCpr.getPassengerSegments();
		for(CheckInAcceptPassengerSegmentDTO cprPassengerSegment : checkInAcceptResponse.getCprJourney().getPassengerSegments()) {
			if(cprPassengerSegment == null) {
				continue;
			}
			
			PassengerSegment findPaxSegment = passengerSegment.stream().filter(paxSegment -> paxSegment != null 
					&& Objects.equals(paxSegment.getPassengerId(), cprPassengerSegment.getPassengerId())
					&& Objects.equals(paxSegment.getSegmentId(), cprPassengerSegment.getSegmentId())).findFirst().orElse(null);
			if (findPaxSegment != null && findPaxSegment.getSeat() != null) {
				cprPassengerSegment.setOriginalSeatNumber(findPaxSegment.getSeat().getSeatNo());
			}
		}
	}
	
	/**
	 * sent boarding pass after check in success.
	 * 
	 * @param checkInAcceptResponse
	 * @param request
	 * @param journey
	 * @throws BusinessBaseException
	 */
	private void sendBoardingPass(CheckInAcceptResponseDTO checkInAcceptResponse, CheckInAcceptRequestDTO request,
			Booking bookingMergedCpr) throws BusinessBaseException {

		if (null == checkInAcceptResponse || null == checkInAcceptResponse.getCprJourney()
				|| !BooleanUtils.isTrue(checkInAcceptResponse.getCprJourney().getAllowMBP())
				|| BooleanUtils.isTrue(checkInAcceptResponse.getCprJourney().getInhibitUSBP())) {
			logger.warn(String.format("Cannot Send boarding pass -> not allow sent boarding pass by rloc: %s.", request.getRloc()));
			return;
		}

		// get check in request passenger list
		List<Passenger> requiredBPPaxList = getRequiredBPPaxList(checkInAcceptResponse, request, bookingMergedCpr);
		boolean hasAnyContact = requiredBPPaxList.stream().anyMatch(this::checkHasContactInfo);
		if (!hasAnyContact) {
			logger.warn(String.format("Cannot Send boarding pass -> all passengers no contact info by rloc: %s.", request.getRloc()));
			return;
		}

		SendBoardingPassResponseDTO responseDTO = new SendBoardingPassResponseDTO();
		try {
			responseDTO = this.sendBoardingPass(request, requiredBPPaxList);
		} catch (Exception e) {
			logger.error(String.format("Send boarding pass failed. by rloc: %s.", request.getRloc()), e);
		}

		convertBoardingPassToDTO(checkInAcceptResponse, responseDTO);
	}
	
	
	/**
	 * check whether has contact info
	 * 
	 * @param passengers
	 * @return
	 */
	private boolean checkHasContactInfo(Passenger passenger) {
		if (null == passenger || null == passenger.getContactInfo() || null == passenger.getContactInfo().getEmail()
				|| null == passenger.getContactInfo().getPhoneInfo()) {
			return false;
		}

		ContactInfo contact = passenger.getContactInfo();

		return !StringUtils.isEmpty(contact.getEmail().getEmailAddress())
				&& !StringUtils.isEmpty(contact.getPhoneInfo().getPhoneCountryNumber())
				&& !StringUtils.isEmpty(contact.getPhoneInfo().getPhoneNo());
	}
	
	
	/**
	 * sent boarding pass to checked in passengers
	 * @param requestDTO
	 * @param journey
	 * @param checkInRequestPaxList
	 */
	private SendBoardingPassResponseDTO sendBoardingPass(CheckInAcceptRequestDTO requestDTO,
			List<Passenger> requiredBPPaxList) {
		// filter has contact info passengers
		List<String> requiredUcis = requiredBPPaxList.stream().filter(this::checkHasContactInfo)
				.map(Passenger::getCprUniqueCustomerId).collect(Collectors.toList());

		SendBoardingPassResponseDTO returnResponse = new SendBoardingPassResponseDTO();
		// get eligible sent boarding pass ucis
		List<String> eligibleBpPaxUcis = new ArrayList<>();
		BoardingPassResponseDTO bpEligibleResponse = new BoardingPassResponseDTO();
		try {
			bpEligibleResponse = sentBPService.getEligibleBPPaxs(requestDTO.getJourneyId(), requiredUcis, false, requestDTO.getRloc());
			if (bpEligibleResponse != null) {
				eligibleBpPaxUcis = bpEligibleResponse.getEligibleBoardingPassPassengerUcis();
				returnResponse.addAllErrors(PnrCprMergeHelper.covertErrorInfos(bpEligibleResponse.getErrors()));
			}
			returnResponse.setEligibleBoardingPassPassengerUcis(eligibleBpPaxUcis);
		} catch (Exception e) {
			logger.error(String.format("Cannot Send boarding pass -> add DCHK faile by rloc: %s.", requestDTO.getRloc()), e);
		}

		// get eligible sent boarding pass passengers
		List<Passenger> eligibleBpPaxList = filterRequestPaxList(eligibleBpPaxUcis, requiredBPPaxList);
		if (CollectionUtils.isEmpty(eligibleBpPaxList)) {
			logger.warn("Send boarding pass failed-> not fond can eligible passenger by rloc: %s." + requestDTO.getRloc());
			return null;
		}

		// send email boarding pass
		sendEmailBoardingPass(requestDTO, returnResponse, eligibleBpPaxList);
		// send sms boarding pass
		sendSMSBoardingPass(requestDTO, returnResponse, eligibleBpPaxList);

		return returnResponse;
	}

	/**
	 * Send SMS boarding pass
	 * 
	 * @param requestDTO
	 * @param returnResponse
	 * @param eligibleBpPaxList
	 */
	private void sendSMSBoardingPass(CheckInAcceptRequestDTO requestDTO, SendBoardingPassResponseDTO returnResponse,
			List<Passenger> eligibleBpPaxList) {
		try {
			SendSMSResponseDTO smsBPRes = sentBPService.sendSms(buildSendSmsRequest(requestDTO.getJourneyId(), eligibleBpPaxList), requestDTO.getRloc());
			if (smsBPRes != null && smsBPRes.getErrors() != null) {
				returnResponse.addAllErrors(PnrCprMergeHelper.covertErrorInfos(smsBPRes.getErrors()));
			} else {
				returnResponse.setSendEmailSMSSuccessFlag(true);
			}
		} catch (Exception e) {
			logger.error(String.format("Send booking[%s] journey[%s] sms boarding pass failed.", requestDTO.getRloc(), requestDTO.getJourneyId()), e);
		}
	}

	/**
	 * send email boarding pass
	 * 
	 * @param requestDTO
	 * @param returnResponse
	 * @param eligibleBpPaxList
	 */
	private void sendEmailBoardingPass(CheckInAcceptRequestDTO requestDTO, SendBoardingPassResponseDTO returnResponse,
			List<Passenger> eligibleBpPaxList) {
		try {
			SendEmailResponseDTO emailBPRes = sentBPService.sendEmail(buildSendEmailRequest(requestDTO.getJourneyId(), eligibleBpPaxList), requestDTO.getRloc());
			if (emailBPRes != null && emailBPRes.getErrors() != null) {
				returnResponse.addAllErrors(PnrCprMergeHelper.covertErrorInfos(emailBPRes.getErrors()));
			}else {
				returnResponse.setSendEmailBPSuccessFlag(true);
			}
		} catch (Exception e) {
			logger.error(String.format("Send booking[%s] journey[%s] email boarding pass failed.", requestDTO.getRloc(), requestDTO.getJourneyId()), e);
		}
	}
	
	/**
	 * Build send SMS request
	 * 
	 * @param journeyId
	 * @param passengers
	 * @return
	 * @throws UnexpectedException
	 */
	private SendSMSRequestDTO buildSendSmsRequest(String journeyId, List<Passenger> passengers) throws UnexpectedException{
		if (StringUtils.isEmpty(journeyId) || CollectionUtils.isEmpty(passengers)) {
			throw new UnexpectedException("Send sms boarding pass failure -> Cannot find journeyId[%s] or can't find any passengers under booking.",new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}

		SendSMSRequestDTO request = new SendSMSRequestDTO();
		List<String> ucis = new ArrayList<>();
		request.setJourneyId(journeyId);
		for (Passenger pax : passengers) {
			if (null == pax.getContactInfo() || null == pax.getContactInfo().getPhoneInfo()
					|| StringUtils.isEmpty(pax.getContactInfo().getPhoneInfo().getPhoneCountryNumber())
					|| StringUtils.isEmpty(pax.getContactInfo().getPhoneInfo().getPhoneNo())) {
				continue;
			}
			ucis.add(pax.getCprUniqueCustomerId());
		}
		//remove duplicate phone numbers
		List<String> distinctUcis = ucis.stream().distinct().collect(Collectors.toList());
		request.setUcis(distinctUcis);
		return request;
	}
	
	/**
	 * Build send email request
	 * 
	 * @param journeyId
	 * @param passengers
	 * @return
	 * @throws UnexpectedException
	 */
	private SendEmailRequestDTO buildSendEmailRequest(String journeyId, List<Passenger> passengers) throws UnexpectedException{
		if (StringUtils.isEmpty(journeyId) || CollectionUtils.isEmpty(passengers)) {
			throw new UnexpectedException("Send email boarding pass failure -> Cannot find journeyId[%s] or can't find any passengers under booking.",new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}

		SendEmailRequestDTO request = new SendEmailRequestDTO();
		List<String> ucis = new ArrayList<>();

		request.setJourneyId(journeyId);
		for (Passenger pax : passengers) {
			if (null == pax.getContactInfo() || null == pax.getContactInfo().getEmail()
					|| StringUtils.isEmpty(pax.getContactInfo().getEmail().getEmailAddress())) {
				continue;
			}
			ucis.add(pax.getCprUniqueCustomerId());
		}
		//remove duplicate email addresses
		List<String> distinctUcis = ucis.stream().distinct().collect(Collectors.toList());
		request.setUcis(distinctUcis);
		
		return request;
	}
	
	/**
	 * filter passengers in requestDTO 
	 * 
	 * @param eligibleBpPaxUcis
	 * @param journey
	 * @return
	 */
	private List<Passenger> filterRequestPaxList(List<String> eligibleBpPaxUcis, List<Passenger> requiredBPPaxList) {
		if (CollectionUtils.isEmpty(eligibleBpPaxUcis) || CollectionUtils.isEmpty(requiredBPPaxList)) {
			return Collections.emptyList();
		}

		return requiredBPPaxList.stream().filter(pax -> eligibleBpPaxUcis.contains(pax.getCprUniqueCustomerId()))
				.collect(Collectors.toList());
	}
	
	/**
	 * build check in required passengerList
	 *
	 * @param journey
	 * @param requestDTO
	 * @return
	 * @throws ExpectedException
	 */
	private List<Passenger> getRequiredBPPaxList(CheckInAcceptResponseDTO checkInAcceptResponse,
			CheckInAcceptRequestDTO requestDTO, Booking bookingMergedCpr) throws ExpectedException {
		List<CheckInAcceptPassengerDTO> journeyPassengers = checkInAcceptResponse.getCprJourney().getPassengers();
		if (journeyPassengers == null || journeyPassengers.isEmpty()) {
			throw new ExpectedException(String.format("no passenger in journey -> journey id[%s].",
					checkInAcceptResponse.getCprJourney().getJourneyId()), new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}

		List<Passenger> passengers= bookingMergedCpr.getPassengers();
		// get uniqueCustomers from requestDTO base on cprJourney
		List<Passenger> resultPaxList = new ArrayList<>();
		for (CheckInAcceptPassengerDTO journeyPassenger : journeyPassengers) {
			if (journeyPassenger.isInhibitBP()) {
				continue;
			}
			
			Passenger passenger = passengers.stream().filter(pax -> pax.getPassengerId().equals(journeyPassenger.getPassengerId())
					&& requestDTO.getPassengerIds().contains(pax.getPassengerId())).findFirst().orElse(null);
			if(null != passenger){
				resultPaxList.add(passenger);
			}
		}

		return resultPaxList;
	}
	
	/**
	 * convert boarding pass response to CheckInAcceptResponseDTO
	 * @param checkInAcceptResponse
	 * @param boardingPass
	 */
	private void convertBoardingPassToDTO(CheckInAcceptResponseDTO checkInAcceptResponse,SendBoardingPassResponseDTO boardingPassResponse) {
		if(boardingPassResponse == null){
			return;
		}

		if(!CollectionUtils.isEmpty(boardingPassResponse.getErrors())){
			checkInAcceptResponse.setErrors(boardingPassResponse.getErrors());
		}
		List<String> eligibleBpPaxUcis = boardingPassResponse.getEligibleBoardingPassPassengerUcis();
		
		// If eligible send boarding pass passenger list not empty, and no business error
		if (!CollectionUtils.isEmpty(eligibleBpPaxUcis) 
				&& (boardingPassResponse.isSendEmailBPSuccessFlag() || boardingPassResponse.isSendEmailSMSSuccessFlag())) {
			checkInAcceptResponse.getCprJourney().getPassengers().stream().forEach(
					pax -> pax.setBoardingPassSent(eligibleBpPaxUcis.contains(pax.getCprUniqueCustomerId())));
		}
	}
	
	/**
	 * Generate check-in reject response
	 * 
	 * @param cprJourney
	 * @return
	 */
	private CheckInAcceptResponseDTO generateCheckInRejectResponse(Journey cprJourney) {
		CheckInAcceptResponseDTO responseDTO = new CheckInAcceptResponseDTO();
		responseDTO.setHasAcceptedSectorOfRequest(false);
		responseDTO.setCprJourney(generateCheckInFailureJourneyDTO(cprJourney));
		return responseDTO;
	}

	/**
	 * Generate Check-in failure JourneyDTO
	 * 
	 * @param cprJourney
	 * @return
	 */
	private CheckInAcceptCprJourneyDTO generateCheckInFailureJourneyDTO(Journey cprJourney) {
		if(cprJourney == null) {
			return null;
		}
		
		CheckInAcceptCprJourneyDTO journeyDTO = new CheckInAcceptCprJourneyDTO();
		journeyDTO.setJourneyId(cprJourney.getJourneyId());
		journeyDTO.setPassengers(generateCheckInRejectPassengers(cprJourney.getPassengers()));
		journeyDTO.setSegments(generateCheckInRejectSegments(cprJourney.getSegments()));
		journeyDTO.setPassengerSegments(generateCheckInRejectPaxSegments(cprJourney.getPassengerSegments()));
		return journeyDTO;
	}

	/**
	 * Generate journey reject passengerSegments
	 * 
	 * @param passengerSegments
	 * @return
	 */
	private List<CheckInAcceptPassengerSegmentDTO> generateCheckInRejectPaxSegments(List<CprJourneyPassengerSegment> passengerSegments) {
		if(CollectionUtils.isEmpty(passengerSegments)) {
			return null;
		}
		
		List<CheckInAcceptPassengerSegmentDTO> passengerSegmentDTOs = new ArrayList<>();
		for(CprJourneyPassengerSegment passengerSegment : passengerSegments) {
			if(passengerSegment == null) {
				continue;
			}
			
			CheckInAcceptPassengerSegmentDTO passengerSegmentDTO = new CheckInAcceptPassengerSegmentDTO();
			passengerSegmentDTO.setPassengerId(passengerSegment.getPassengerId());
			passengerSegmentDTO.setCprUniqueCustomerId(passengerSegment.getCprUniqueCustomerId());
			
			passengerSegmentDTO.setSegmentId(passengerSegment.getSegmentId());
			passengerSegmentDTO.setCprProductIdentifierDID(passengerSegment.getCprProductIdentifierDID());
			passengerSegmentDTO.setCprProductIdentifierJID(passengerSegment.getCprProductIdentifierJID());
			// Set reject[not accepted] flag
			passengerSegmentDTO.setCheckedIn(false);
			passengerSegmentDTOs.add(passengerSegmentDTO);
		} 
		
		return passengerSegmentDTOs;
	}

	/**
	 * Generate journey reject Segments
	 * 
	 * @param segments
	 * @return
	 */
	private List<CheckInAcceptCprSegmentDTO> generateCheckInRejectSegments(List<CprJourneySegment> segments) {
		if(CollectionUtils.isEmpty(segments)) {
			return null;
		}
		
		List<CheckInAcceptCprSegmentDTO> segmentDTOs = new ArrayList<>();
		for(CprJourneySegment segment : segments) {
			if(segment == null) {
				continue;
			}
			
			CheckInAcceptCprSegmentDTO segmentDTO = new CheckInAcceptCprSegmentDTO();
			segmentDTO.setSegmentId(segment.getSegmentId());
			segmentDTOs.add(segmentDTO);
		}
		
		return segmentDTOs;
	}

	/**
	 * Generate journey reject passengers
	 * 
	 * @param cprPassengers
	 * @return
	 */
	private List<CheckInAcceptPassengerDTO> generateCheckInRejectPassengers(List<CprJourneyPassenger> cprPassengers) {
		if(CollectionUtils.isEmpty(cprPassengers)) {
			return null;
		}
		
		List<CheckInAcceptPassengerDTO> passengerDTOs = new ArrayList<>();
		for(CprJourneyPassenger passenger : cprPassengers) {
			if(passenger == null) {
				continue;
			}
			
			CheckInAcceptPassengerDTO passengerDTO = new CheckInAcceptPassengerDTO();
			passengerDTO.setPassengerId(passenger.getPassengerId());
			passengerDTO.setCprUniqueCustomerId(passenger.getCprUniqueCustomerId());
			passengerDTO.setCheckedIn(false); //Set passenger reject[not accepted] flag
			passengerDTO.setInhibitBP(passenger.isInhibitBP());
			
			passengerDTOs.add(passengerDTO);
		}
			
		return passengerDTOs;
	}
	
}
