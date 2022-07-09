package com.cathaypacific.mmbbizrule.cxservice.plusgrade.service;

import java.util.List;

import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

public interface PlusGradeService {
	
	public String getPlusGradeUrl(Booking booking, List<String> eligibleSegmentIds, LoginInfo loginInfo, String language, RetrievePnrBooking retrievePnrBooking, boolean isOlci);

}
