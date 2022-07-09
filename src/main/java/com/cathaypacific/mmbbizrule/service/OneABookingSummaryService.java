package com.cathaypacific.mmbbizrule.service;

import java.util.List;
import java.util.concurrent.Future;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummary;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummaryConvertBean;

public interface OneABookingSummaryService {
	
	Future<FlightBookingSummary> asyncGetFlightBookingSummary(FlightBookingSummaryConvertBean summary, LoginInfo loginInfo,  BookingBuildRequired required) throws BusinessBaseException;
	
	/**
	 * @param memberId
	 * @return
	 * @throws BusinessBaseException 
	 */
	public List<FlightBookingSummaryConvertBean> getOneABookingList(String memberId) throws BusinessBaseException;
	
	/**
	 * Async to get booking list
	 * @param memberId
	 * @return
	 * @throws BusinessBaseException
	 */
	public Future<List<FlightBookingSummaryConvertBean>> asyncGetOneABookingList(String memberId) throws BusinessBaseException;
	
	
	/**
	 * Async to get member self booking list
	 * @param summary
	 * @param loginInfo
	 * @param required
	 * @return Future<Booking>
	 * @throws BusinessBaseException
	 */
	public Future<Booking> asyncGetSelfBookings(FlightBookingSummaryConvertBean summary, LoginInfo loginInfo, BookingBuildRequired required) throws BusinessBaseException;

}
