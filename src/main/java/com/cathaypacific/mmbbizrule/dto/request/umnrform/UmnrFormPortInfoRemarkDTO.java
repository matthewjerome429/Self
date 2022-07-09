package com.cathaypacific.mmbbizrule.dto.request.umnrform;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class UmnrFormPortInfoRemarkDTO {

	@NotBlank(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String airportCode;
	
	@Valid
	private UmnrFormGuardianInfoRemarkDTO guardianInfo;

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public UmnrFormGuardianInfoRemarkDTO getGuardianInfo() {
		return guardianInfo;
	}

	public void setGuardianInfo(UmnrFormGuardianInfoRemarkDTO guardianInfo) {
		this.guardianInfo = guardianInfo;
	}
	
}
