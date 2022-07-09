package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.Updated;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

import io.swagger.annotations.ApiModel;

@ApiModel
public class PhoneInfoDTO extends Updated{

	private static final long serialVersionUID = -2308357863921422376L;
	
	/** SUPPORT BACKWARD COMPATIBLE: REMOVE VALIDATION & ADD BACK COUNTRYCODE **/
	// @NotEmpty
	@Pattern(regexp = "^[0-9]*$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String phoneCountryNumber;
	
	private String countryCode;
	
	
	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
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

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	
}
