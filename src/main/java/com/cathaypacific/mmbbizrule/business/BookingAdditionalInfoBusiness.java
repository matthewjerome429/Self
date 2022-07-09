package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.bookingproperties.additional.BookingAdditionalInfoRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.additional.BookingAdditionalInfoResponseDTO;

public interface BookingAdditionalInfoBusiness {
	/**
	 * get booking additional info
	 * 
	 * @param loginInfo
	 * @param rloc
	 * @param language
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	public BookingAdditionalInfoResponseDTO getBookingAdditional(LoginInfo loginInfo, String rloc, String language,
			BookingAdditionalInfoRequestDTO requestDTO) throws BusinessBaseException;
	
	
	
}
