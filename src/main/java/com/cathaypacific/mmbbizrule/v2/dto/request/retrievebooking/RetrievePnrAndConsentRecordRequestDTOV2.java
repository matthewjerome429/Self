package com.cathaypacific.mmbbizrule.v2.dto.request.retrievebooking;

import javax.validation.constraints.AssertTrue;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class RetrievePnrAndConsentRecordRequestDTOV2 {
	
	@AssertTrue(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private boolean consentBoxCheck;

	public boolean isConsentBoxCheck() {
		return consentBoxCheck;
	}

	public void setConsentBoxCheck(boolean consentBoxCheck) {
		this.consentBoxCheck = consentBoxCheck;
	}

}
