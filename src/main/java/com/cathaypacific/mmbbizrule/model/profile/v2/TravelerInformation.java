package com.cathaypacific.mmbbizrule.model.profile.v2;

import java.util.List;

public class TravelerInformation {
	
	private String knownTravelerNumber;
	
	private String redressNumber;
	
	private EmergencyContact emergencyContact;
	
	private String countryOfResidence;

	private List<ProfileTravelDocV2> customerTravelDocument;
	
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

	public EmergencyContact getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(EmergencyContact emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public List<ProfileTravelDocV2> getCustomerTravelDocument() {
		return customerTravelDocument;
	}

	public void setCustomerTravelDocument(List<ProfileTravelDocV2> customerTravelDocument) {
		this.customerTravelDocument = customerTravelDocument;
	}
	
}
