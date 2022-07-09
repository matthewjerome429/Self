package com.cathaypacific.mmbbizrule.business.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.UpdateSeatBusiness;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.PaxSeatDetail;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.updateseat.UpdateSeatResponseDTO;
import com.cathaypacific.mmbbizrule.handler.DTOConverter;
import com.cathaypacific.mmbbizrule.handler.FlightBookingConverterHelper;
import com.cathaypacific.mmbbizrule.handler.MaskHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.KeepSeatPaymentService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.UpdateSeatService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;

/**
 *
 * OLSS-MMB
 *
 * @Desc update seat information for passenger
 * @author fengfeng.jiang
 * @date Jan 26, 2018 3:18:20 PM
 * @version V1.0
 */
@Service
public class UpdateSeatBusinessImpl implements UpdateSeatBusiness {
	
    protected LogAgent logger = LogAgent.getLogAgent(UpdateSeatBusinessImpl.class);

	@Autowired
	private UpdateSeatService updateSeatService;

	@Autowired
	private BookingBuildService bookingBuildService;

	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;

	@Autowired
	private KeepSeatPaymentService keepSeatPaymentService;

	@Autowired
	private DTOConverter dtoConverter;

	@Autowired
	private PnrInvokeService pnrInvokeService;

	@Autowired
	private FlightBookingConverterHelper flightBookingConverterHelper;

	@Autowired
	private MaskHelper maskHelper;

	@Override
	public UpdateSeatResponseDTO updateSeat(LoginInfo loginInfo, UpdateSeatRequestDTO requestDTO) throws BusinessBaseException {
		// retrieve pnrBooking
		RetrievePnrBooking pnrBookingBefore = pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc());
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBookingBefore);

		// please note that this build only build the necessary part of booking for now, if anything more is required in the future, please change the "required"
		Booking bookingBefore = bookingBuildService.buildBooking(pnrBookingBefore, loginInfo, getRequiredForBookinBeforeBuild());

		//update seat
		RetrievePnrBooking pnrBookingAfter = updateSeatService.updateSeat(requestDTO, loginInfo, bookingBefore);



		UpdateSeatResponseDTO response = new UpdateSeatResponseDTO();
		// OLSSMMB-16763: Don't remove me!!!
		logUpdate(requestDTO, bookingBefore);
		if(pnrBookingAfter == null){
			FlightBookingDTO booking = flightBookingConverterHelper.flightBookingDTOConverter(pnrBookingBefore, loginInfo, new BookingBuildRequired(), false);
			lockAndMaskForFlightBookingDTO(true, booking);
			response.setBooking(booking);
		} else {
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBookingAfter);
			Booking bookingAfter = bookingBuildService.buildBooking(pnrBookingAfter, loginInfo,
					new BookingBuildRequired());
			/** if user try to update a purchased seat to another seat of same type, 1A may not be able to add payment for the new seat on time, so we need to transfer the payment manually */
			FlightBookingDTO booking = dtoConverter.convertToBookingDTO(
					keepSeatPaymentService.keepSeatPayment(bookingBefore, bookingAfter), loginInfo);
			lockAndMaskForFlightBookingDTO(true, booking);
			response.setBooking(booking);
		}

		return response;
	}

	/**
	 * log seat update
	 * @param requestDTO
	 * @param bookingBefore
	 */
	private void logUpdate(UpdateSeatRequestDTO requestDTO, Booking bookingBefore) {
		if(bookingBefore != null) {
			Segment updatedSegment = getSegmentWithSegmentId(bookingBefore.getSegments(), requestDTO.getSegmentId());

			if(updatedSegment != null) {
				String displayedClassName = BizRulesUtil.getDisplayedClassNameWithCabinClass(updatedSegment.getCabinClass());

				for(PaxSeatDetail paxSeatDetail: requestDTO.getPaxSeatDetails()) {
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
	 * @return
	 */
	private BookingBuildRequired getRequiredForBookinBeforeBuild() {
		BookingBuildRequired required = new BookingBuildRequired();
		required.setBaggageAllowances(false);
		required.setCprCheck(false);
		required.setCountryOfResidence(false);
		required.setEmergencyContactInfo(false);
		required.setMealSelection(false);
		required.setMemberAward(false);
		required.setOperateInfoAndStops(true);
		required.setPassenagerContactInfo(false);
		required.setRtfs(true);
		required.setSeatSelection(true);
		required.setTravelDocument(false);
		return required;
	}

	/**
	 * @param loginInfo
	 * @param maskValue
	 * @param flightBookingDTO
	 * @throws UnexpectedException
	 */
	private void lockAndMaskForFlightBookingDTO(boolean maskValue, FlightBookingDTO flightBookingDTO){
		// Check the response DTO if the group is empty will set it as
		//LockUtil.updateEmptyGroupAsUnlocked(loginInfo.getMmbToken(), flightBookingDTO, mbTokenCacheRepository)

		//mask user info
		if (maskValue) {
			maskBookingInfo(flightBookingDTO);
		}
	}

	/**
	 * mask booking dto
	 *
	 * @param rloc
	 * @param mmbToken
	 * @param flightBookingDTO
	 * @throws UnexpectedException
	 */
	private void maskBookingInfo(FlightBookingDTO flightBookingDTO) {
		// mask user info
		maskHelper.mask(flightBookingDTO);
	}
}
