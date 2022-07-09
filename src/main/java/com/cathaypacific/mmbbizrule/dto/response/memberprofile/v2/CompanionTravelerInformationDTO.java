package com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2;

public class CompanionTravelerInformationDTO {

	private String knownTravelerNumber;
	
	private String redressNumber;
	
	private EmergencyContactDTO emergencyContact;
	
	private String countryOfResidence;
	
	private String title;
	
	private String gender;
	
	// format DDMMYYYY
	private String dateOfBirth;
	
	private PhoneNumberDTO contactMobileNumber;
	
	private String contactEmailAddress;
	
	private TravelDocumentDTO companionTravelDocument;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public PhoneNumberDTO getContactMobileNumber() {
		return contactMobileNumber;
	}

	public void setContactMobileNumber(PhoneNumberDTO contactMobileNumber) {
		this.contactMobileNumber = contactMobileNumber;
	}

	public String getContactEmailAddress() {
		return contactEmailAddress;
	}

	public void setContactEmailAddress(String contactEmailAddress) {
		this.contactEmailAddress = contactEmailAddress;
	}

	public TravelDocumentDTO getCompanionTravelDocument() {
		return companionTravelDocument;
	}

	public void setCompanionTravelDocument(TravelDocumentDTO companionTravelDocument) {
		this.companionTravelDocument = companionTravelDocument;
	}

}
