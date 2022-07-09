package com.cathaypacific.mmbbizrule.dto.request.umnrform;

import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class UmnrFormAddressRemarkDTO {
	
	@Pattern(regexp = "^[a-zA-Z0-9-./!@*:_ ,+=?()|& ]{0,35}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String building;
	
	@Pattern(regexp = "^[a-zA-Z0-9-./!@*:_ ,+=?()|& ]{0,35}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String street;
	
	@Pattern(regexp = "^[a-zA-Z0-9-./!@*:_ ,+=?()|& ]{0,35}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String city;
	
	@Pattern(regexp = "^[a-zA-Z]{3}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String countryCode;

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String country) {
		this.countryCode = country;
	}
	
}
