package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2;

import java.util.List;

public class CustomerTravelerInformationResponse {

	private String knownTravelerNumber;
	
	private String redressNumber;
	
	private EmergencyContactResponse emergencyContact;
	
	private String name;
	
	private PhoneNumberResponse phoneNumber;
	
	private String countryOfResidence;
	
	private List<TravelDocumentResponse> customerTravelDocument;

	public String getKnownTravelerNumber() {
		return knownTravelerNumber;
	}

	public void setKnownTravelerNumber(String knownTravelerNumber) {
		this.knownTravelerNumber = knownTravelerNumber;
	}

	public String getRedressNumber() {
		return redressNumber;
	}

	public void setRedressNumber(String redressNumber) {
		this.redressNumber = redressNumber;
	}

	public EmergencyContactResponse getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(EmergencyContactResponse emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

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

	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public List<TravelDocumentResponse> getCustomerTravelDocument() {
		return customerTravelDocument;
	}

	public void setCustomerTravelDocument(List<TravelDocumentResponse> customerTravelDocument) {
		this.customerTravelDocument = customerTravelDocument;
	}

}
