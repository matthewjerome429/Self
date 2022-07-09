package com.cathaypacific.mmbbizrule.dto.request.umnrform;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.validator.constraints.PhoneNum;

public class UmnrFormGuardianInfoRemarkDTO {

	@NotBlank(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String name;
	
	private String relationship;
	
	@Valid
	private UmnrFormAddressRemarkDTO address;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@PhoneNum(message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private UMNRFormPhoneInfoRemarkDTO phoneInfo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UmnrFormAddressRemarkDTO getAddress() {
		return address;
	}

	public void setAddress(UmnrFormAddressRemarkDTO address) {
		this.address = address;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public UMNRFormPhoneInfoRemarkDTO getPhoneInfo() {
		return phoneInfo;
	}

	public void setPhoneInfo(UMNRFormPhoneInfoRemarkDTO phoneInfo) {
		this.phoneInfo = phoneInfo;
	}

}
