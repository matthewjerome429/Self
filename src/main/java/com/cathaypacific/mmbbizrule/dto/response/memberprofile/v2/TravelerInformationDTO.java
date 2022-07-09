package com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2;

import java.util.List;

public class TravelerInformationDTO {

	private String knownTravelerNumber;
	
	private String redressNumber;
	
	private EmergencyContactDTO emergencyContact;
	
	private String countryOfResidence;
	
	private String gender;
	
	// format DDMMYYYY
	private String dateOfBirth;
	
	private List<TravelDocumentDTO> customerTravelDocument;
	
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

	public EmergencyContactDTO getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(EmergencyContactDTO emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public List<TravelDocumentDTO> getCustomerTravelDocument() {
		return customerTravelDocument;
	}

	public void setCustomerTravelDocument(List<TravelDocumentDTO> customerTravelDocument) {
		this.customerTravelDocument = customerTravelDocument;
	}

}
