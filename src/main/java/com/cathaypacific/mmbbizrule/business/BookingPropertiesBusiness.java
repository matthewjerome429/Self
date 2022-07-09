package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.bookingcustomizedinfo.BookingCustomizedInfoRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.memberselfbookings.SelfBookingsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.BookingCustomizedInfoResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingCommercePropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.journey.JoruneyResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberselfbookings.SelfBookingsResponseDTO;

public interface BookingPropertiesBusiness {
	
	/**
	 * get booking properties by RLOC
	 * @param rloc
	 * @param plusgrade 
	 * @return
	 * @throws BusinessBaseException 
	 */
	public BookingPropertiesDTO getBookingProperties(LoginInfo loginInfo, String rloc) throws BusinessBaseException;
	
	/**
	 * Get booking properties by RLOC for E-commerce
	 * @param rloc
	 * @return
	 * @throws BusinessBaseException 
	 */
	public BookingCommercePropertiesDTO getBookingCommerceProperties(LoginInfo loginInfo, String rloc) throws BusinessBaseException;
	
	/**
	 *Get booking journeys  
	 * @param rloc
	 * @return
	 * @throws BusinessBaseException
	 */
	public JoruneyResponseDTO getBookingJourneys(String oneARloc) throws BusinessBaseException;
	
	/**
	 * Get booking basic info  
	 * @param request
	 * @return BookingPropertiesDTO
	 * @throws BusinessBaseException 
	 */
	public BookingCustomizedInfoResponseDTO getBookingCustomizedInfo(BookingCustomizedInfoRequestDTO request) throws BusinessBaseException;
	
	/**
	 * Get self booking list  
	 * @param request
	 * @param isRetrieveAll
	 * @return BookingBasicInfoResponseDTO
	 * @throws BusinessBaseException 
	 */
	public SelfBookingsResponseDTO getSelfBookingList(SelfBookingsRequestDTO requestDTO,boolean includeCompanionbooking) throws BusinessBaseException;
	
}
