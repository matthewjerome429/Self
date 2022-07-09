package com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

public class EmailAddressDTOV2  implements Serializable{
	
	private static final long serialVersionUID = -8690803373481469075L;

	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String passengerId;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String uniqueCustomerId;
	
	@NotNull
	@Pattern(regexp = "^([\\w\\.\\-\\_])+@(([\\w\\-\\_])+\\.)+(\\w)+$", groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String emailAddress;

	
	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getUniqueCustomerId() {
		return uniqueCustomerId;
	}

	public void setUniqueCustomerId(String uniqueCustomerId) {
		this.uniqueCustomerId = uniqueCustomerId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
