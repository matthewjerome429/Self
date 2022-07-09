package com.cathaypacific.mmbbizrule.v2.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;

@Component
public class FlightBookingConverterHelperV2 {
	
	
	@Autowired
	private BookingBuildService bookingBuildService;
	
	@Autowired
	private DTOConverterV2 dtoConverter;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private MaskHelperV2 maskHelper;
	
	/**
	 * convert RetrievePnrBooking to FlightBookingDTO
	 * @param pnrBooking
	 * @param loginInfo
	 * @param required
	 * @param paxIdenRequired - if need to do pax identification
	 * @return FlightBookingDTOV2
	 * @throws BusinessBaseException
	 */
	public FlightBookingDTOV2 flightBookingDTOConverter(RetrievePnrBooking pnrBooking,LoginInfo loginInfo, BookingBuildRequired required, boolean paxIdenRequired) throws BusinessBaseException{
		if(paxIdenRequired) {
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		}
		
		// build booking
		Booking booking = bookingBuildService.buildBooking(pnrBooking, loginInfo, required);
		
		// booking convert to flightBookingDTO
		FlightBookingDTOV2 flightBookingDTO = dtoConverter.convertToBookingDTO(booking, loginInfo);
			
		//mask data
        maskBookingInfo(flightBookingDTO);
        
		return flightBookingDTO;
	}
	
	/**
	 * mask booking dto
	 * @param rloc
	 * @param mmbToken
	 * @param flightBookingDTO
	 * @throws UnexpectedException 
	 */
    private void maskBookingInfo(FlightBookingDTOV2 flightBookingDTO) {
		// mask user info
    	maskHelper.mask(flightBookingDTO);
	}
    
    /**
     * Convert to booking model
     * @param pnrBooking
     * @param loginInfo
     * @param required
     * @param paxIdenRequired
     * @return
     * @throws BusinessBaseException
     */
	public Booking getFlightBooking(RetrievePnrBooking pnrBooking, LoginInfo loginInfo, BookingBuildRequired required, boolean paxIdenRequired) throws BusinessBaseException {
		if (paxIdenRequired) {
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		}
		// build booking
		return bookingBuildService.buildBooking(pnrBooking, loginInfo, required);
	}
    
  
}
