package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.updateseat.UpdateSeatResponseDTO;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc update seat information for passenger
 * @author fengfeng.jiang
 * @date Jan 26, 2018 2:54:21 PM
 * @version V1.0
 */
public interface UpdateSeatBusiness {
	/** 
	 * @Description update seat
	 * @param loginInfo
	 * @param requestDTO
	 * @return UpdateSeatResponseDTO
	 * @author fengfeng.jiang
	 */
	UpdateSeatResponseDTO updateSeat(LoginInfo loginInfo, UpdateSeatRequestDTO requestDTO) throws BusinessBaseException;
	
}
