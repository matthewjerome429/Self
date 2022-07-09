package com.cathaypacific.mmbbizrule.service;

import java.util.List;
import java.util.concurrent.Future;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummaryConvertBean;

public interface EodsBookingSummaryService {

	/**
	 * Get Eods booking List by memebr id
	 * @param memberId
	 * @return
	 */
	public List<FlightBookingSummaryConvertBean> getEodsBookingList(LoginInfo loginInfo, String token) throws BusinessBaseException ;
	
	/**
	 * Async to get Eods booking List by memebr id
	 * @param memberId
	 * @return
	 */
	public Future<List<FlightBookingSummaryConvertBean>> asynGetEodsBookingList(LoginInfo loginInfo, String token) throws BusinessBaseException ;

}
