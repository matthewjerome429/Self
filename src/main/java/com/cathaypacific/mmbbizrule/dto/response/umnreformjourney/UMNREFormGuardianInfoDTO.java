package com.cathaypacific.mmbbizrule.dto.response.umnreformjourney;

public class UMNREFormGuardianInfoDTO {

	private String name;
	
	private String countryCode;
	
	private String relationship;
	
	private String phoneCountryNumber;
	
	private String phoneNumber;
	
	private UMNREFormAddressDTO address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getPhoneCountryNumber() {
		return phoneCountryNumber;
	}

	public void setPhoneCountryNumber(String phoneCountryNumber) {
		this.phoneCountryNumber = phoneCountryNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public UMNREFormAddressDTO getAddress() {
		return address;
	}

	public void setAddress(UMNREFormAddressDTO address) {
		this.address = address;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	
	
}
