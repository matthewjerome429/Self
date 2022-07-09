package com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2;

public class EmergencyContactDTO {

	private String name;
	
	private PhoneNumberDTO phoneNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PhoneNumberDTO getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(PhoneNumberDTO phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
