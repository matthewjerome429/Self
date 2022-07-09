package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.UpdateAssistanceRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.cancelassistance.CancelAssistanceRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.RequestAssistanceDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.UpdateAssistanceResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.specialservice.cancelassistance.CancelAssistanceResponseDTO;

public interface RequestAssistanceBusiness {

	/**
	 * Get special service info for Request assistance page
	 * @param rloc
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	public RequestAssistanceDTO getRequestAssistanceInfo(String rloc, LoginInfo loginInfo) throws BusinessBaseException;
	
	/**
	 * Add new special service in Request assistance page
	 * @return
	 */
	public UpdateAssistanceResponseDTO updateAssistance(UpdateAssistanceRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException;

	/**
	 * Cancel special service: assistance
	 * 
	 * @param requestDTO
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	public CancelAssistanceResponseDTO cancelAssistance(CancelAssistanceRequestDTO requestDTO, LoginInfo loginInfo) throws BusinessBaseException;

}
