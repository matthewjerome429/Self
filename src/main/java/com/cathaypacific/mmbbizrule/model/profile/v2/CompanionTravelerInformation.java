package com.cathaypacific.mmbbizrule.model.profile.v2;

public class CompanionTravelerInformation {
	
	private String knownTravelerNumber;
	
	private String redressNumber;
	
	private EmergencyContact emergencyContact;
	
	private String countryOfResidence;
	
	private String title;
	
	private String gender;
	
	// format DDMMYYYY
	private String dateOfBirth;
	
	private PhoneNumber contactMobileNumber;
	
	private String contactEmailAddress;
	
	private ProfileTravelDocV2 companionTravelDocument;

	
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

	public void setCompanionTravelDocument(ProfileTravelDocV2 companionTravelDocument) {
		this.companionTravelDocument = companionTravelDocument;
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

	public PhoneNumber getContactMobileNumber() {
		return contactMobileNumber;
	}

	public void setContactMobileNumber(PhoneNumber contactMobileNumber) {
		this.contactMobileNumber = contactMobileNumber;
	}

	public String getContactEmailAddress() {
		return contactEmailAddress;
	}

	public void setContactEmailAddress(String contactEmailAddress) {
		this.contactEmailAddress = contactEmailAddress;
	}

	public ProfileTravelDocV2 getCompanionTravelDocument() {
		return companionTravelDocument;
	}

}
