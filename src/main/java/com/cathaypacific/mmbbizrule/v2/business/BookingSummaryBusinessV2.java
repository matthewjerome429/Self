package com.cathaypacific.mmbbizrule.v2.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary.BookingSummaryResponseDTOV2;

public interface BookingSummaryBusinessV2 {
	
	/**
	 * Get member's bookings, it inclould temp linked, eods, and 1A bookings 
	 * @param loginInfo
	 * @param required
	 * @return
	 * @throws BusinessBaseException
	 */
	public BookingSummaryResponseDTOV2 getMemberBookings(LoginInfo loginInfo,BookingBuildRequired required) throws BusinessBaseException;

}
