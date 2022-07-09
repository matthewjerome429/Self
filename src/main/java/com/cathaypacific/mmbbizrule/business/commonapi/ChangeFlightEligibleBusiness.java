package com.cathaypacific.mmbbizrule.business.commonapi;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ChangeFlightResponseDTO;
import com.cathaypacific.mmbbizrule.dto.request.changeflight.ChangeFlightDataRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.changeflight.ChangeFlightDataResponseDTO;

public interface ChangeFlightEligibleBusiness {

	/**
	 * Check change flight eligible by RLOC
	 * @param rloc
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	ChangeFlightResponseDTO changeFlightEligibleByRloc(String rloc,LoginInfo loginInfo) throws BusinessBaseException;

	/**
	 * Get change flight data
	 * @param requestDTO
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	ChangeFlightDataResponseDTO getChangeFlightData(ChangeFlightDataRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException;
	
}