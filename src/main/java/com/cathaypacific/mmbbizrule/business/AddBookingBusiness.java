package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.addbooking.AddBookingRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.addbooking.AddBookingResponseDTO;

public interface AddBookingBusiness {
	
	/**
	 * add booking for member
	 * 
	 * @param loginInfo
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	public AddBookingResponseDTO addBooking(LoginInfo loginInfo, AddBookingRequestDTO requestDTO) throws BusinessBaseException;
	
}
