package com.cathaypacific.mmbbizrule.dto.request.umnrform;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.Updated;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

public class UMNRFormPhoneInfoRemarkDTO extends Updated{

	private static final long serialVersionUID = -2308357863921422376L;

	@NotEmpty
	@Pattern(regexp = "^[0-9]*$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String phoneCountryNumber;
	
	@NotNull
	@Pattern(regexp = "^[0-9]{0,15}$", groups= {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String phoneNo;

	public String getPhoneCountryNumber() {
		return phoneCountryNumber;
	}

	public void setPhoneCountryNumber(String phoneCountryNumber) {
		this.phoneCountryNumber = phoneCountryNumber;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

}
