package com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.Updated;

public class UpdateCountryOfResidenceDTOV2 extends Updated {

	private static final long serialVersionUID = -5091970313306612226L;

	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String countryCode;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public boolean isBlank() {
		return StringUtils.isEmpty(countryCode);
	}

}
