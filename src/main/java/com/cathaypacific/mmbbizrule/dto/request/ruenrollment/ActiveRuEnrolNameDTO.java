package com.cathaypacific.mmbbizrule.dto.request.ruenrollment;

import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

public class ActiveRuEnrolNameDTO {
	
	@ApiModelProperty(required = true, value = "family name")
	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String familyName;

	@ApiModelProperty(required = true, value = "given name")
	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String givenName;
	
	@ApiModelProperty(required = true, value = "name title")
	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String title;

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
