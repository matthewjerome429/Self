package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.response.bookingsummary.BookingSummaryResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;

public interface BookingSummaryBusiness {

	public BookingSummaryResponseDTO getMemberBookings(LoginInfo loginInfo, String lastRloc, String pageSize, BookingBuildRequired required) throws BusinessBaseException;

}
