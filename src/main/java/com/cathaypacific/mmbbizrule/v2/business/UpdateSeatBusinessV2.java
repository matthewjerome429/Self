package com.cathaypacific.mmbbizrule.v2.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.v2.dto.request.updateseat.UpdateSeatRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.updateseat.UpdateSeatResponseDTOV2;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc update seat information for passenger
 * @author fengfeng.jiang
 * @date Jan 26, 2018 2:54:21 PM
 * @version V1.0
 */
public interface UpdateSeatBusinessV2 {
	/** 
	 * @Description update seat
	 * @param loginInfo
	 * @param requestDTO
	 * @return UpdateSeatResponseDTO
	 * @author fengfeng.jiang
	 */
	UpdateSeatResponseDTOV2 updateSeat(LoginInfo loginInfo, UpdateSeatRequestDTOV2 requestDTO) throws BusinessBaseException;
}
