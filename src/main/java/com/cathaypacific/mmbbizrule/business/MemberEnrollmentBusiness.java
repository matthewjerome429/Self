package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.ruenrollment.ActiveRuEnrolRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.ruenrollment.ActiveRuEnrolResponseDTO;

public interface MemberEnrollmentBusiness {
	
	/**
	 * enrol active RU account
	 * @return RuEnrolResponseDTO
	 * @throws BusinessBaseException 
	 */
	public ActiveRuEnrolResponseDTO enrolActiveRuAccount(ActiveRuEnrolRequestDTO request, LoginInfo loginInfo) throws BusinessBaseException;
}
