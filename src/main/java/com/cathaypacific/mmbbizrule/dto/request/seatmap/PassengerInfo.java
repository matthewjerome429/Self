package com.cathaypacific.mmbbizrule.dto.request.seatmap;

import org.springframework.util.StringUtils;

import io.swagger.annotations.ApiModelProperty;

public class PassengerInfo {
	
	@ApiModelProperty(required = false)
	private String familyName;
	
	@ApiModelProperty(required = false)
	private String givenName;
	
	@ApiModelProperty(required = false)
	private String passengerId;
	
	@ApiModelProperty(required = false)
	private String passengerType;

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(this.familyName) && StringUtils.isEmpty(this.givenName)
				&& StringUtils.isEmpty(this.passengerId) && StringUtils.isEmpty(this.passengerType);
	}
}
