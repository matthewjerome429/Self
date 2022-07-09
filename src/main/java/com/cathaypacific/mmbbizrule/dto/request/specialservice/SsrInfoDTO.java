package com.cathaypacific.mmbbizrule.dto.request.specialservice;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class SsrInfoDTO {

	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String ssrCode;
	
	private String additionalInfo;

	public String getSsrCode() {
		return ssrCode;
	}

	public void setSsrCode(String ssrCode) {
		this.ssrCode = ssrCode;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
}
