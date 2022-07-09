package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.LinkTempBookingResponseDTO;

public interface BookingLinkBusiness {
	/**
	 * Check booking and store to temp booking list if valid
	 * @param rloc
	 * @param familyName
	 * @param givenName
	 * @return
	 * @throws BusinessBaseException 
	 */
	public LinkTempBookingResponseDTO linkBooking(LoginInfo loginInfo, String rloc,String familyName, String givenName) throws BusinessBaseException; 
}
