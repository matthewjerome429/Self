package com.cathaypacific.mmbbizrule.v2.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.addbooking.AddBookingRequestDTO;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.AddLinkedBookingDTO;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CheckLinkedBookingDTO;

public interface LinkBookingBusiness {
	
	/**
	 * check linked booking for staff
	 * 
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	public CheckLinkedBookingDTO checkLinkedBooking(LoginInfo loginInfo, AddBookingRequestDTO requestDTO) throws BusinessBaseException;
	
	/**
     * add linked booking for staff
     * 
     * @param loginInfo
     * @param requestDTO
     * @return
     * @throws BusinessBaseException
     */
    public AddLinkedBookingDTO addLinkedBooking(LoginInfo loginInfo, AddBookingRequestDTO requestDTO) throws BusinessBaseException;
	
}
