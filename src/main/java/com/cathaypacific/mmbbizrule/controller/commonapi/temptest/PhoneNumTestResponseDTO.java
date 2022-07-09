package com.cathaypacific.mmbbizrule.controller.commonapi.temptest;

public class PhoneNumTestResponseDTO {
	
	private boolean validPhoneNumber = false;
	
	private String formattedNumber;
	
	private String ios2CountryCode;
	
	private String ios3CountryCode;
	
	private String countryNumber;
	
	private String phoneNumber;
	
	private String type;

	public String getFormattedNumber() {
		return formattedNumber;
	}

	public void setFormattedNumber(String formattedNumber) {
		this.formattedNumber = formattedNumber;
	}

	public String getIos2CountryCode() {
		return ios2CountryCode;
	}

	public void setIos2CountryCode(String ios2CountryCode) {
		this.ios2CountryCode = ios2CountryCode;
	}

	public String getIos3CountryCode() {
		return ios3CountryCode;
	}

	public void setIos3CountryCode(String ios3CountryCode) {
		this.ios3CountryCode = ios3CountryCode;
	}

	public String getCountryNumber() {
		return countryNumber;
	}

	public void setCountryNumber(String countryNumber) {
		this.countryNumber = countryNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isValidPhoneNumber() {
		return validPhoneNumber;
	}

	public void setValidPhoneNumber(boolean validPhoneNumber) {
		this.validPhoneNumber = validPhoneNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
 
}
