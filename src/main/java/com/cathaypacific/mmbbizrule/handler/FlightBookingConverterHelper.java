package com.cathaypacific.mmbbizrule.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;

@Component
public class FlightBookingConverterHelper {
	
	
	@Autowired
	private BookingBuildService bookingBuildService;
	
	@Autowired
	private DTOConverter dtoConverter;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private MaskHelper maskHelper;
	
	/**
	 * convert RetrievePnrBooking to FlightBookingDTO
	 * @param pnrBooking
	 * @param loginInfo
	 * @param required
	 * @param paxIdenRequired - if need to do pax identification
	 * @return FlightBookingDTO
	 * @throws BusinessBaseException
	 */
	public FlightBookingDTO flightBookingDTOConverter(RetrievePnrBooking pnrBooking,LoginInfo loginInfo, BookingBuildRequired required, boolean paxIdenRequired) throws BusinessBaseException{
		if(paxIdenRequired) {
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		}
		
		// build booking
		Booking booking = bookingBuildService.buildBooking(pnrBooking, loginInfo, required);
		
		// booking convert to flightBookingDTO
		FlightBookingDTO flightBookingDTO = dtoConverter.convertToBookingDTO(booking, loginInfo);
			
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
    private void maskBookingInfo(FlightBookingDTO flightBookingDTO) {
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
