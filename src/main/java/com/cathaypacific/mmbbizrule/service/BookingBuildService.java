package com.cathaypacific.mmbbizrule.service;

import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

@Service
public interface BookingBuildService {

	public Booking buildBooking(RetrievePnrBooking booking, LoginInfo loginInfo, BookingBuildRequired required) throws BusinessBaseException;

}
