package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2;

public class EmergencyContactResponse {

	private String name;
	
	private PhoneNumberResponse phoneNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PhoneNumberResponse getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(PhoneNumberResponse phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
