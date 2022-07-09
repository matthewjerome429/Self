package com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp;


import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.annotation.IsUpdated;
import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.validator.constraints.PhoneNum;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

public class PhoneNumbersDTOV2 {
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String passengerId;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String uniqueCustomerId;
	
	@IsUpdated
	@PhoneNum(groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private PhoneNumberDTOV2 phoneNumber;
	
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

	public PhoneNumberDTOV2 getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(PhoneNumberDTOV2 phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public PhoneNumberDTOV2 findPhoneNumber() {
		if(phoneNumber == null){
			phoneNumber = new PhoneNumberDTOV2();
		}
		return phoneNumber;
	}
}
