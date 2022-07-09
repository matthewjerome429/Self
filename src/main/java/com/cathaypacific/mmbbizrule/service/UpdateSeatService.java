package com.cathaypacific.mmbbizrule.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to update seat for passenger in PNR
 * @author fengfeng.jiang
 * @date Jan 29, 2018 5:19:37 PM
 * @version V1.0
 */
public interface UpdateSeatService {
	/**
	 * 
	* @Description update seat
	* @param
	* @return RetrievePnrBooking
	* @author fengfeng.jiang
	 * @param map 
	 */
	public RetrievePnrBooking updateSeat(UpdateSeatRequestDTO requestDTO, LoginInfo loginInfo, Booking booking) throws BusinessBaseException;
}
