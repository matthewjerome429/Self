package com.cathaypacific.mmbbizrule.dto.request.tokendata.transfer;

import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

public class TransferTokenDataRequestDTO {
	
	@ApiModelProperty(required = true, value = "the key of the stored token data")
	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
