package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2;

public class PhoneNumberResponse {

	private String countryCode;
	
	private String iso;
	
	private String number;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getIso() {
		return iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}
