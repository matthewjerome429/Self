package com.cathaypacific.mmbbizrule.dto.request.ruenrollment;

import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

public class ActiveRuEnrolLoginDetailsDTO {
	
	@ApiModelProperty(required = true, value = "PIN")
	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String pin;

	@ApiModelProperty(required = false, value = "user name, if null, will use email as default user name")
	private String userName;

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
