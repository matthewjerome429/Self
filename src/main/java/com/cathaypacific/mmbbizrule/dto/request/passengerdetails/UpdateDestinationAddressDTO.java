package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.Updated;

import io.swagger.annotations.ApiModel;

@ApiModel
public class UpdateDestinationAddressDTO extends Updated{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6237907855086270855L;

	private String transitFlag;
	
	private boolean transit;
	
	@Pattern(regexp = "^[a-zA-Z0-9 ]{0,35}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String streetName;
	
	@Pattern(regexp = "^[a-zA-Z0-9 ]{0,35}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String city;
	
	@NotEmpty
	private String stateCode;
	
	@NotEmpty
	private String stateName;
	
	@Pattern(regexp = "^[a-zA-Z0-9]{0,17}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String zipCode;
	
	public String getTransitFlag() {
		return transitFlag;
	}

	public void setTransitFlag(String transitFlag) {
		this.transitFlag = transitFlag;
	}

	public boolean isTransit() {
		return transit;
	}

	public void setTransit(boolean transit) {
		this.transit = transit;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	@Override
	public boolean isBlank() {
		return StringUtils.isEmpty(transitFlag) && StringUtils.isEmpty(streetName) && StringUtils.isEmpty(city) && StringUtils.isEmpty(stateCode) && StringUtils.isEmpty(zipCode);
	}

}
