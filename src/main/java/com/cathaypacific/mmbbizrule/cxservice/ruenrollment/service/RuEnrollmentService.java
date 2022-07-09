package com.cathaypacific.mmbbizrule.cxservice.ruenrollment.service;

import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.RuEnrolRequest;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.response.RuEnrolResponse;

public interface RuEnrollmentService {

	/**
	 * create active RU account
	 * @param string 
	 * @param requestDTO
	 * @return RuEnrolResponseDTO
	 */
	public RuEnrolResponse enrolActiveRuAccount(RuEnrolRequest request, String appCode);
}
