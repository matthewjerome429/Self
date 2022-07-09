package com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;


public class PhoneNumberDTOV2 {
	@Pattern(regexp = "^[0-9]*$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String countryCode;

	@NotNull
	@Pattern(regexp = "^[0-9]{0,15}$", groups= {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String number;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
