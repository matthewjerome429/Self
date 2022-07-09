package com.cathaypacific.mmbbizrule.v2.business.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.PaxSeatDetail;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.KeepSeatPaymentService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.UpdateSeatService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.v2.business.UpdateSeatBusinessV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.updateseat.PaxSeatDetailDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.updateseat.UpdateSeatRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.updateseat.UpdateSeatResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.handler.DTOConverterV2;
import com.cathaypacific.mmbbizrule.v2.handler.MaskHelperV2;
import com.cathaypacific.mmbbizrule.v2.service.UpdateSeatServiceV2;
import com.cathaypacific.olciconsumer.model.response.changeseat.ChangeSeatPassengerDTO;
import com.cathaypacific.olciconsumer.model.response.changeseat.ChangeSeatResponseDTO;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

/**
 *
 * OLSS-MMB
 *
 * @Desc update seat information for passenger
 * @author haiwei.jia
 * @version V2.0
 */
@Service
public class UpdateSeatBusinessImplV2 implements UpdateSeatBusinessV2 {
	
	private static LogAgent logger = LogAgent.getLogAgent(UpdateSeatBusinessImplV2.class);

	@Autowired
	private UpdateSeatService updateSeatService;
	
	@Autowired
	private UpdateSeatServiceV2 updateSeatServiceV2;

	@Autowired
	private BookingBuildService bookingBuildService;

	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;

	@Autowired
	private KeepSeatPaymentService keepSeatPaymentService;

	@Autowired
	private DTOConverterV2 dtoConverter;

	@Autowired
	private PnrInvokeService pnrInvokeService;

	@Autowired
	private MaskHelperV2 maskHelper;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;

	@Override
	public UpdateSeatResponseDTOV2 updateSeat(LoginInfo loginInfo, UpdateSeatRequestDTOV2 requestDTO) throws BusinessBaseException {
		
		UpdateSeatResponseDTOV2 response = new UpdateSeatResponseDTOV2();
		/** if the cprGot from cache is false, shouldn't retrieve info from CPR and should update to PNR. this check is for the case: if user enters seatMap before check-in time and wait to update until is in check-in time, should update the seat to PNR */
		boolean cprGot  = BooleanUtils.isTrue(mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.GET_CPR_SUCCESS, requestDTO.getRloc(), Boolean.class));
		
		// retrieve pnrBooking
		RetrievePnrBooking pnrBookingBefore = pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc());
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBookingBefore);
		
		// please note that this build only build the necessary part of booking for now, if anything more is required in the future, please change the "required"
		Booking bookingBefore = bookingBuildService.buildBooking(pnrBookingBefore, loginInfo, getRequiredForBookinBeforeBuild(cprGot));
		
		if (bookingBefore == null) {
			throw new ExpectedException(String.format("Cannot Update seat info because of retrieve booking failed, rloc: %s", requestDTO.getRloc()), new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		Segment updatedSegment = getSegmentWithSegmentId(bookingBefore.getSegments(), requestDTO.getSegmentId());
		List<Journey> cprJourneys = bookingBefore.getCprJourneys();
		
		// if should use CPR session in the booking retrievement after update
		boolean useCprSession = true;
		RetrievePnrBooking pnrBookingAfter = null;
		// if the segment post check-in or flown, cannot update the seats
		if (isSegmentPostCheckInOrFlown(requestDTO.getSegmentId(), bookingBefore)){
			throw new ExpectedException("Cannot Update seat info because sector post check-in time or is flown", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		// if is not within check in window, update the seat to PNR
		else if (!isWithInCheckInWindow(requestDTO.getSegmentId(), cprJourneys)) {
			//update seat
			pnrBookingAfter = updateSeatService.updateSeat(buildRequest(requestDTO), loginInfo, bookingBefore);
			// OLSS-6335 tagging
			tagging(bookingBefore, requestDTO, "MMB", "All Time");
		} 
		// if can get segment info from CPR journey and all passengerSegments are checked in, then update to CPR
		else if (allPassengerSegmentsCheckedIn(requestDTO, cprJourneys)) {
			ChangeSeatResponseDTO changeResult;
			try {
				changeResult = updateSeatServiceV2.changeSeat(requestDTO, cprJourneys);
				useCprSession = false;
			} catch (ExpectedException e) {
				response.addError(e.getErrorInfo());
				return response;
			}
			// olci tries to send BP in the change seat API, check the BP sending result
			checkBPSendingResult(response, changeResult);
			
		} 
		// if all passengerSegments are not yet checked in, temporarily save the seat in cache
		else if (allPassengerSegmentsCanCheckInAndNotCheckedIn(requestDTO, cprJourneys)) {
			updateSeatServiceV2.saveTempSeat(requestDTO, bookingBefore);
			// OLSS-6335 tagging
			tagging(bookingBefore, requestDTO, "OLCI", null);
		} 
		// if the request is invalid, e.g. one passenger checked in and one passenger not yet checked in, can't update the seat
		else {
			throw new ExpectedException("Cannot Update seat info because request invalid", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}

		
		// OLSSMMB-16763: Don't remove me!!!
		logUpdate(requestDTO, updatedSegment);
		
		BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
		bookingBuildRequired.setUseCprSession(useCprSession);
		if(pnrBookingAfter == null){
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBookingBefore);
			// build booking
			Booking booking = bookingBuildService.buildBooking(pnrBookingBefore, loginInfo, bookingBuildRequired);
			
			// booking convert to flightBookingDTO
			FlightBookingDTOV2 flightBookingDTO = dtoConverter.convertToBookingDTO(booking, loginInfo);
				
			//mask data
	        maskBookingInfo(flightBookingDTO);
			response.setBooking(flightBookingDTO);
		} else {
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBookingAfter);
			Booking bookingAfter = bookingBuildService.buildBooking(pnrBookingAfter, loginInfo, bookingBuildRequired);
			/** if user try to update a purchased seat to another seat of same type, 1A may not be able to add payment for the new seat on time, so we need to transfer the payment manually */
			FlightBookingDTOV2 flightBookingDTO = dtoConverter.convertToBookingDTO(
					keepSeatPaymentService.keepSeatPayment(bookingBefore, bookingAfter), loginInfo);
			//mask data
	        maskBookingInfo(flightBookingDTO);
			response.setBooking(flightBookingDTO);
		}

		return response;
	}

	/**
	 * check BP sending result after the seat change
	 * @param response
	 * @param changeResult
	 */
	private void checkBPSendingResult(UpdateSeatResponseDTOV2 response, ChangeSeatResponseDTO changeResult) {
		boolean resendBPRequired = false, resendBPSuccess = false;
		/** currently, MMB only support one pax in each seat update request, so assume there's only one pax and use anyMatch to identify the result */
		if (changeResult != null && !CollectionUtils.isEmpty(changeResult.getPassengers())) {
			// if any of the passenger is eligible to send BP, "resendBPRequired" is true
			resendBPRequired = anyPassengerEligibleForBp(changeResult);
			// if any of the passenger send BP success, "resendBPSuccess" is true
			resendBPSuccess = anyPassengerResendBPSuccess(changeResult);
		}
		response.setResendBPRequired(resendBPRequired);
		response.setResendBPSuccess(resendBPRequired ? resendBPSuccess : null);
	}

	/**
	 * check if there's any passenger sending BP success
	 * @param changeResult
	 * @return boolean
	 */
	private boolean anyPassengerResendBPSuccess(ChangeSeatResponseDTO changeResult) {
		return changeResult.getPassengers().stream().anyMatch(ChangeSeatPassengerDTO :: isBoardingPassSent);
	}

	/**
	 * check if there's any passenger eligible for BP sending
	 * @param changeResult
	 * @return boolean
	 */
	private boolean anyPassengerEligibleForBp(ChangeSeatResponseDTO changeResult) {
		return changeResult.getPassengers().stream().anyMatch(ChangeSeatPassengerDTO :: isBpEligible);
	}

	/**
	 * log seat update
	 * @param requestDTO
	 * @param updatedSegment
	 */
	private void logUpdate(UpdateSeatRequestDTOV2 requestDTO, Segment updatedSegment) {
		if(updatedSegment != null) {
			String displayedClassName = BizRulesUtil.getDisplayedClassNameWithCabinClass(updatedSegment.getCabinClass());

			for(PaxSeatDetailDTOV2 paxSeatDetail: requestDTO.getPaxSeatDetails()) {
				// Once the paxSeatDetail cannot find seat No but the seat preference exists, it means the client is updating seat preference
				boolean isUpdatingSeatPreference = !StringUtils.isEmpty(paxSeatDetail.getSeatPreference()) && StringUtils.isEmpty(paxSeatDetail.getSeatNo());

				if (!isUpdatingSeatPreference){
					logger.info(String.format("Update | Seat No | Rloc | %s | Passenger ID | %s | Segment ID | %s | Class | %s",
							requestDTO.getRloc(),
							paxSeatDetail.getPassengerID(),
							requestDTO.getSegmentId(),
							displayedClassName), true);
				} else {
                    logger.info(String.format("Update | Seat Preference | Rloc | %s | Passenger ID | %s | Segment ID | %s | Class | %s",
                            requestDTO.getRloc(),
                            paxSeatDetail.getPassengerID(),
                            requestDTO.getSegmentId(),
                            displayedClassName), true);
                }
			}
		}
	}

	/**
	 * OLSS-6335 tagging
	 * 
	 * # FOC seat selected (first time selection) for sector being checked in
	 *   FOC Seat | {seatNumber} | Update | First Time | OLCI | RLOC | {XXXXXX} | passengerId | {1} | segmentId | {2}

	 *# FOC seat reselected (second time selection) for sector being checked in
	 *  FOC Seat | {seatNumber} | Update | Second Time | OLCI | RLOC | {XXXXXX} | passengerId | {1} | segmentId | {2}

	 *# FOC seat selected for sector not being checked in
	 *  FOC Seat | {seatNumber} | Update | All Time | MMB | RLOC | {XXXXXX} | passengerId | {1} | segmentId | {2}
	 * 
	 * @param booking
	 * @param requestDTO
	 * @param flow
	 * @param time
	 */
	private void tagging(Booking booking, UpdateSeatRequestDTOV2 requestDTO, String flow, String time) {
		if(booking == null || requestDTO == null
				|| CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(requestDTO.getPaxSeatDetails())) {
			return;
		}
		
		String rloc = requestDTO.getRloc();
		String segmentId = requestDTO.getSegmentId();
		for(PaxSeatDetailDTOV2 paxSeatDetail : requestDTO.getPaxSeatDetails()) {
			if(paxSeatDetail == null || StringUtils.isEmpty(paxSeatDetail.getPassengerID())) {
				continue;
			}
			
			String passengerId = paxSeatDetail.getPassengerID();
			String seatNo = paxSeatDetail.getSeatNo();
			PassengerSegment pasengerSegment = BookingBuildUtil.getPassegnerSegmentByIds(booking.getPassengerSegments(), passengerId, segmentId);
			
			if(pasengerSegment != null && StringUtils.isEmpty(time)) {
				if(pasengerSegment.getOriginalSeat() != null && StringUtils.isNotEmpty(pasengerSegment.getOriginalSeat().getSeatNo())) {
					time = "Second Time";
				} else {
					time = "First Time";
				}
			}
			
			logger.info(String.format("FOC Seat | %s | Update | %s | %s | RLOC | %s | passengerId | %s | segmentId | %s",
					seatNo, time, flow, rloc, passengerId, segmentId), true);
		}
	}

	/**
	 * check if the segment is post check-in or flown
	 * @param segmentId
	 * @param bookingBefore
	 * @return
	 */
	private boolean isSegmentPostCheckInOrFlown(String segmentId, Booking bookingBefore) {
		if (CollectionUtils.isEmpty(bookingBefore.getSegments())) {
			return false;
		}
		 Segment segment = bookingBefore.getSegments().stream().filter(seg -> segmentId.contentEquals(seg.getSegmentID())).findFirst().orElse(null);
		 if (segment == null) {
			return false;
		} else {
			return segment.isPostCheckIn() || BooleanUtils.isTrue(segment.isFlown());
		}
	}

	/**
	 * build UpdateSeatRequestDTOV2 to UpdateSeatRequestDTO
	 * @param requestDTO
	 * @return UpdateSeatRequestDTO
	 */
	private UpdateSeatRequestDTO buildRequest(UpdateSeatRequestDTOV2 requestDTO) {
		UpdateSeatRequestDTO request = new UpdateSeatRequestDTO();
		request.setRloc(requestDTO.getRloc());
		request.setSegmentId(requestDTO.getSegmentId());
		if (!CollectionUtils.isEmpty(requestDTO.getPaxSeatDetails())) {
			List<PaxSeatDetail> paxSeatDetails = new ArrayList<>();
			for (PaxSeatDetailDTOV2 paxSeatDetailDTO : requestDTO.getPaxSeatDetails()) {
				PaxSeatDetail paxSeatDetail = new PaxSeatDetail();
				paxSeatDetail.setPassengerID(paxSeatDetailDTO.getPassengerID());
				paxSeatDetail.setSeatNo(paxSeatDetailDTO.getSeatNo());
				paxSeatDetail.setSeatPreference(paxSeatDetailDTO.getSeatPreference());
				paxSeatDetails.add(paxSeatDetail);
			}
			request.setPaxSeatDetails(paxSeatDetails);
		}
		return request;
	}

	/**
	 * check if all passengerSegmets can check in and not checked in
	 * @param requestDTO
	 * @param cprJourneys
	 * @return boolean
	 */
	private boolean allPassengerSegmentsCanCheckInAndNotCheckedIn(UpdateSeatRequestDTOV2 requestDTO,
			List<Journey> cprJourneys) {
		String segmentId = requestDTO.getSegmentId();
		return requestDTO.getPaxSeatDetails().stream().allMatch(seatDetail -> passengerSegmentCanCheckIn(cprJourneys,seatDetail.getPassengerID(), segmentId)
				&& !passengerSegmentCheckedIn(cprJourneys, seatDetail.getPassengerID(), segmentId));
	}

	/**
	 * check if all passengerSegments are checked in
	 * @param requestDTO
	 * @param cprJourneys
	 * @return boolean
	 */
	private boolean allPassengerSegmentsCheckedIn(UpdateSeatRequestDTOV2 requestDTO, List<Journey> cprJourneys) {		
		return requestDTO.getPaxSeatDetails().stream().allMatch(seatDetail -> passengerSegmentCheckedIn(cprJourneys,
				seatDetail.getPassengerID(), requestDTO.getSegmentId()));
	}

	/**
	 * check if the passengerSegment can check in according to cprJourneys
	 * @param cprJourneys
	 * @param passengerId
	 * @param segmentId
	 * @return boolean
	 */
	private boolean passengerSegmentCanCheckIn(List<Journey> cprJourneys, String passengerId, String segmentId) {
		// get cprPassengerSegment from cprJourneys
		CprJourneyPassengerSegment cprPassengerSegment = getCprPassengerSegment(cprJourneys, passengerId, segmentId);
		if (cprPassengerSegment == null) {
			return false;
		} else {
			return cprPassengerSegment.getCanCheckIn();
		}
	}
	
	/**
	 * check if the passengerSegment is checked in according to cprJourneys
	 * @param cprJourneys
	 * @param passengerId
	 * @param segmentId
	 * @return boolean
	 */
	private boolean passengerSegmentCheckedIn(List<Journey> cprJourneys, String passengerId, String segmentId) {
		// get cprPassengerSegment from cprJourneys
		CprJourneyPassengerSegment cprPassengerSegment = getCprPassengerSegment(cprJourneys, passengerId, segmentId);
		if (cprPassengerSegment == null) {
			return false;
		} else {
			return cprPassengerSegment.getCheckedIn();
		}
	}
	
	/**
	 * get cprPassengerSegmet from cprJourneys
	 * @param cprJourneys
	 * @param passengerId
	 * @param segmentId
	 * @return boolean
	 */
	private CprJourneyPassengerSegment getCprPassengerSegment(List<Journey> cprJourneys, String passengerId, String segmentId) {
		CprJourneyPassengerSegment cprPassengerSegment = null;
		// find cprPassengerSegment by passengerId and segmentId
		for (Journey cprJourney : cprJourneys) {
			if (CollectionUtils.isEmpty(cprJourney.getPassengerSegments())) {
				continue;
			}
			cprPassengerSegment = getCprPassengerSegmentByIds(cprJourney.getPassengerSegments(), passengerId, segmentId);
			if (cprPassengerSegment != null) {
				break;
			}
		}
		return cprPassengerSegment;
	}

	/**
	 * get CPR passengerSegment by passengerId and segmentId
	 * @param passengerSegments
	 * @param passengerId
	 * @param segmentId
	 * @return CprJourneyPassengerSegment
	 */
	private CprJourneyPassengerSegment getCprPassengerSegmentByIds(List<CprJourneyPassengerSegment> passengerSegments,
			String passengerId, String segmentId) {
		if (CollectionUtils.isEmpty(passengerSegments)) {
			return null;
		}
		
		return passengerSegments.stream()
				.filter(ps -> !StringUtils.isEmpty(ps.getPassengerId()) && !StringUtils.isEmpty(ps.getSegmentId())
						&& ps.getPassengerId().equals(passengerId) && ps.getSegmentId().equals(segmentId))
				.findFirst().orElse(null);
	}

	/**
	 * check if can get CPR info for the segment
	 * @param segmentId
	 * @param cprJourneys
	 * @return boolean
	 */
	private boolean isWithInCheckInWindow(String segmentId, List<Journey> cprJourneys) {
		return !CollectionUtils.isEmpty(cprJourneys) && cprJourneys.stream().anyMatch(
				journey -> journey.isOpenToCheckIn() && journey.isCanCheckIn()
						&& journey.getSegments().stream().anyMatch(seg -> segmentId.equals(seg.getSegmentId())));
	}
	
	// get segment with its ID
	private Segment getSegmentWithSegmentId(List<Segment> segmentList, String segmentId) {
	    // return null if the list is empty
	    if(segmentList == null || segmentList.isEmpty()) {
	        return null;
        }
	    // find the related segment based on the given segmentId
	    for (int i = 0; i < segmentList.size(); i++){
	        Segment currentSegment = segmentList.get(i);
	        if(StringUtils.equals(currentSegment.getSegmentID(), segmentId)) {
	            return currentSegment;
            }
        }
	    return null;
    }

	/**
	 * get required for build of bookingBefore
	 * @param cprGot 
	 * @return BookingBuildRequired
	 */
	private BookingBuildRequired getRequiredForBookinBeforeBuild(boolean cprGot) {
		BookingBuildRequired required = new BookingBuildRequired();
		required.setBaggageAllowances(false);
		required.setCprCheck(cprGot);
		required.setCountryOfResidence(false);
		required.setEmergencyContactInfo(false);
		required.setMealSelection(false);
		required.setMemberAward(false);
		required.setOperateInfoAndStops(true);
		required.setPassenagerContactInfo(false);
		required.setRtfs(true);
		required.setSeatSelection(true);
		required.setTravelDocument(false);
		required.setUseCprSession(true);
		return required;
	}

	/**
	 * mask booking dto
	 *
	 * @param rloc
	 * @param mmbToken
	 * @param flightBookingDTO
	 * @throws UnexpectedException
	 */
	private void maskBookingInfo(FlightBookingDTOV2 flightBookingDTO) {
		// mask user info
		maskHelper.mask(flightBookingDTO);
	}
}
