package com.cathaypacific.mmbbizrule.model.profile;

public class ProfileTravelDoc {
	private String documentNumber;

	// format:"yyyy-MM-dd"
	private String expiryDate;

	private String familyName;

	private String givenName;

	private String issueCountry;
	
	private String issueCountryIosThree;
	
	private String nationality;
	
	private String nationalityIosThree;
	
	private String type;

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getIssueCountry() {
		return issueCountry;
	}

	public void setIssueCountry(String issueCountry) {
		this.issueCountry = issueCountry;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIssueCountryIosThree() {
		return issueCountryIosThree;
	}

	public void setIssueCountryIosThree(String issueCountryIosThree) {
		this.issueCountryIosThree = issueCountryIosThree;
	}

	public String getNationalityIosThree() {
		return nationalityIosThree;
	}

	public void setNationalityIosThree(String nationalityIosThree) {
		this.nationalityIosThree = nationalityIosThree;
	}

	
}
