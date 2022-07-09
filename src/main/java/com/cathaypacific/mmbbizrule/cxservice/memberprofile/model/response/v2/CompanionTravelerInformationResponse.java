package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2;

public class CompanionTravelerInformationResponse {

	private String knownTravelerNumber;
	
	private String redressNumber;
	
	private EmergencyContactResponse emergencyContact;
	
	private String countryOfResidence;
	
	private String title;
	
	private String gender;
	
	// format DDMMYYYY
	private String dateOfBirth;
	
	private PhoneNumberResponse contactMobileNumber;
	
	private String contactEmailAddress;
	
	private TravelDocumentResponse companionTravelDocument;

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

	public PhoneNumberResponse getContactMobileNumber() {
		return contactMobileNumber;
	}

	public void setContactMobileNumber(PhoneNumberResponse contactMobileNumber) {
		this.contactMobileNumber = contactMobileNumber;
	}

	public String getContactEmailAddress() {
		return contactEmailAddress;
	}

	public void setContactEmailAddress(String contactEmailAddress) {
		this.contactEmailAddress = contactEmailAddress;
	}

	public TravelDocumentResponse getCompanionTravelDocument() {
		return companionTravelDocument;
	}

	public void setCompanionTravelDocument(TravelDocumentResponse companionTravelDocument) {
		this.companionTravelDocument = companionTravelDocument;
	}
	
}
