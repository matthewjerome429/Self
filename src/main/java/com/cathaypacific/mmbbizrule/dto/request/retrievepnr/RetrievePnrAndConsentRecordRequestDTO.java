package com.cathaypacific.mmbbizrule.dto.request.retrievepnr;

import javax.validation.constraints.AssertTrue;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class RetrievePnrAndConsentRecordRequestDTO {
	
	@AssertTrue(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private boolean consentBoxCheck;

	public boolean isConsentBoxCheck() {
		return consentBoxCheck;
	}

	public void setConsentBoxCheck(boolean consentBoxCheck) {
		this.consentBoxCheck = consentBoxCheck;
	}

}
