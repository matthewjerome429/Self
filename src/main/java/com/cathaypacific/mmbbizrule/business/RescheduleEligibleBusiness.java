package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.response.reschedule.BookingRescheduleResponseDTO;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc update seat information for passenger
 * @author fengfeng.jiang
 * @date Jan 26, 2018 2:54:21 PM
 * @version V1.0
 */
public interface RescheduleEligibleBusiness {
	/** 
	 * @Description update seat
	 * @param loginInfo
	 * @param requestDTO
	 * @return UpdateSeatResponseDTO
	 * @author fengfeng.jiang
	 */
	BookingRescheduleResponseDTO rescheduleEligibleByRloc(String rloc,LoginInfo loginInfo,String languageLocale) throws BusinessBaseException;
	
}
