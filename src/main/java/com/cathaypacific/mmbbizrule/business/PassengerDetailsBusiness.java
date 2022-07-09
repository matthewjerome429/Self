package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.passengerdetails.PassengerDetailsResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;

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
public interface PassengerDetailsBusiness {

	/**
	 * 
	 * Update passenger info
	 * 
	 * @param
	 * @return PassengerDetailsResponseDTO
	 * @author fengfeng.jiang
	 */
	public PassengerDetailsResponseDTO updatePaxDetails(LoginInfo loginInfo,
			UpdatePassengerDetailsRequestDTO request, BookingBuildRequired required) throws BusinessBaseException;

}
