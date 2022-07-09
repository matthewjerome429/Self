package com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2;

public class TravelDocumentDTO {

	private String travelDocumentType;

	private String familyName;

	private String givenName;

	private String nationality;

	private String countryOfIssue;

	private String travelDocumentNo;

	// @JsonFormat(pattern="DDMMYYYY") ==> (pattern="yyyy-MM-dd")
	private String expiryDate;
	
	private String preferredTravelDocumentIndicator;
	
	private String trustedTravelDocumentIndicator;

	public String getTravelDocumentType() {
		return travelDocumentType;
	}

	public void setTravelDocumentType(String travelDocumentType) {
		this.travelDocumentType = travelDocumentType;
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

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getCountryOfIssue() {
		return countryOfIssue;
	}

	public void setCountryOfIssue(String countryOfIssue) {
		this.countryOfIssue = countryOfIssue;
	}

	public String getTravelDocumentNo() {
		return travelDocumentNo;
	}

	public void setTravelDocumentNo(String travelDocumentNo) {
		this.travelDocumentNo = travelDocumentNo;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPreferredTravelDocumentIndicator() {
		return preferredTravelDocumentIndicator;
	}

	public void setPreferredTravelDocumentIndicator(String preferredTravelDocumentIndicator) {
		this.preferredTravelDocumentIndicator = preferredTravelDocumentIndicator;
	}

	public String getTrustedTravelDocumentIndicator() {
		return trustedTravelDocumentIndicator;
	}

	public void setTrustedTravelDocumentIndicator(String trustedTravelDocumentIndicator) {
		this.trustedTravelDocumentIndicator = trustedTravelDocumentIndicator;
	}

}
