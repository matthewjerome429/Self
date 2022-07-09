package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import io.swagger.annotations.ApiModel;

@ApiModel
public class UpdateEmergencyContactDTO extends PhoneInfoDTO{

	private static final long serialVersionUID = 2071068479955640202L;

	@Pattern(regexp = "^[a-zA-Z ]{1,64}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String contactName;
	
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	@Override
	public boolean isBlank() {
		return StringUtils.isEmpty(getPhoneCountryNumber()) && StringUtils.isEmpty(contactName) && StringUtils.isEmpty(getPhoneNo());
	}
}
