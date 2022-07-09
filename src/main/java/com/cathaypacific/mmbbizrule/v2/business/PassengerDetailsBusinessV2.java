package com.cathaypacific.mmbbizrule.v2.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.passengerdetails.PassengerDetailsResponseDTOV2;

/**
 * 
 * OLSS-MMB
 * 
 * This service is used to update passenger info of PNR
 * 
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:50:16 PM
 * @version V1.0
 */
public interface PassengerDetailsBusinessV2 {

	/**
	 * 
	 * Update passenger info
	 * 
	 * @param
	 * @return PassengerDetailsResponseDTO
	 * @author fengfeng.jiang
	 */
	public PassengerDetailsResponseDTOV2 updatePaxDetails(LoginInfo loginInfo,
			UpdatePassengerDetailsRequestDTOV2 request, BookingBuildRequired required) throws BusinessBaseException;

}
