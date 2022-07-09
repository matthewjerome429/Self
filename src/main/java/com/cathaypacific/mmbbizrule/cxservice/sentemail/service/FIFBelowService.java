package com.cathaypacific.mmbbizrule.cxservice.sentemail.service;

import com.cathaypacific.mmbbizrule.dto.request.email.FIFBelowRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.email.FIFBelowReponseDTO;

public interface FIFBelowService {
	
	/**
	 * retrieve 15Blow from fifBlowService by request
	 * 
	 * @param request
	 * @return boolean
	 */
	FIFBelowReponseDTO sentEmail(FIFBelowRequestDTO request);	
}
