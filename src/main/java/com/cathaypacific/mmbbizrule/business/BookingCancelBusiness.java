package com.cathaypacific.mmbbizrule.business;

import java.util.Locale;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.bookingcancel.CancelBookingDataRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancel.BookingCancelResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancel.CancelBookingDataResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcancelcheck.BookingCancelCheckResponseDTO;

public interface BookingCancelBusiness {
	/**
	 * Check booking Can cancel
	 * @return
	 * @throws BusinessBaseException 
	 */
	public BookingCancelCheckResponseDTO checkBookingCanCancel(String rloc, LoginInfo loginInfo, boolean skipIbeCheck) throws BusinessBaseException;
	
	/**
	 * Cancel booking, will auto logout if non member login
	 * @param rloc
	 * @param refund 
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	public BookingCancelResponseDTO cancelBooking(String rloc, boolean refund, LoginInfo loginInfo, Locale locale) throws BusinessBaseException;
	
	/**
	 * 
	 * @param requestDTO
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException 
	 */
	public CancelBookingDataResponseDTO getCancelBookingData(CancelBookingDataRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException;
}
