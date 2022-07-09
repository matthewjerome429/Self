package com.cathaypacific.mmbbizrule.dto.request.tokendata.store;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

public class StoreTokenDataRequestDTO {

	@ApiModelProperty(required = true, value = "6-digit 1A rloc")
	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "^[A-Za-z0-9]{6}$",message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String rloc;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}
}
