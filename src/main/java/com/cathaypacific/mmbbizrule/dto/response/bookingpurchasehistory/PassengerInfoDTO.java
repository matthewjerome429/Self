package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class PassengerInfoDTO implements Serializable{
	
	private static final long serialVersionUID = -6199905294065142220L;
	@ApiModelProperty(value = "passenger id", required = true)
	private String passengerId;
	@ApiModelProperty(value = "family name", required = true)
	private String familyName;
	@ApiModelProperty(value = "given name", required = true)
	private String givenName;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

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

}
