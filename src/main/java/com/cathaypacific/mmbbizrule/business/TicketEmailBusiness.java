package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.email.SendEmailRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.email.SendEmailReponseDTO;

public interface TicketEmailBusiness {
	
	/**
	 * send ticket e-mail
	 * 
	 * @param requestDTO
	 * @param loginInfo
	 * @return SendEmailReponseDTO
	 * @throws BusinessBaseException
	 */
	public SendEmailReponseDTO sendTicketEmail(SendEmailRequestDTO requestDTO, LoginInfo loginInfo)
			throws BusinessBaseException;
	
}
