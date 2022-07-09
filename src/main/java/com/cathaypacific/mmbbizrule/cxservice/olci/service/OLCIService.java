package com.cathaypacific.mmbbizrule.cxservice.olci.service;

import java.util.List;
import java.util.concurrent.Future;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.request.CancelCheckInRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.response.CancelCheckInResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.PassengerCheckInInfo;
import com.cathaypacific.mmbbizrule.dto.response.cancelcheckin.BookingCancelCheckInResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

public interface OLCIService {

	 List<PassengerCheckInInfo> getBRLPaxDetails(LoginInfo loginInfo, Booking booking);
	
	 Future<List<PassengerCheckInInfo>> asyncGetBRLPaxDetails(LoginInfo loginInfo, Booking booking);

	 CancelCheckInResponseDTO cancelCheckInWithOLCI(CancelCheckInRequestDTO cancelCheckInRequestDTO,String cookie);

	 BookingCancelCheckInResponseDTO cancelCheckInByLogin(LoginInfo loginInfo,String rloc) throws BusinessBaseException, InterruptedException;

	void sendJourneyBP(List<String> acceptSegmentIds, RetrievePnrBooking retrievePnrBooking, String rloc,
			String givenName, String familyName);

}
