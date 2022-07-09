package com.cathaypacific.mmbbizrule.dto.request.email;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

public class PaxEmailInfoDTO {
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String passengerId;
	
	private String familyName;
	
	private String givenName;
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Email(groups = {MaskFieldGroup.class}, regexp = "^([\\w\\.\\-\\_])+@(([\\w\\-\\_])+\\.)+(\\w)+$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String email;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
