package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.feedback.SaveFeedbackRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.feedback.SaveFeedbackResponseDTO;

public interface SaveFeedbackBusiness {
	
	/**
	 * 
	* @Description save feed back
	* @param loginInfo
	* @param requestDTO
	* @return SaveFeedbackResponseDTO
	* @author haiwei.jia
	 */
	public SaveFeedbackResponseDTO saveFeedback(LoginInfo loginInfo, SaveFeedbackRequestDTO requestDTO) throws BusinessBaseException;
}
