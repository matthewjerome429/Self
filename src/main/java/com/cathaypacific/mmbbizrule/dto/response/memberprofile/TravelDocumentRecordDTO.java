package com.cathaypacific.mmbbizrule.dto.response.memberprofile;

import io.swagger.annotations.ApiModelProperty;

public class TravelDocumentRecordDTO {

	private String documentNumber;

	// @JsonFormat(pattern="yyyy-MM-dd")
	private String expiryDate;

	private String familyName;

	private String givenName;

	private String issueCountry;

	private String nationality;

	private String type;

	private String gender;

	private String dateOfBirth;
	
	@ApiModelProperty(value = "True if it is member travel doc. False if it is companion travel doc", required = true)
	private boolean memberTravelDoc;

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

	public boolean isMemberTravelDoc() {
		return memberTravelDoc;
	}

	public void setMemberTravelDoc(boolean isMemberTravelDoc) {
		this.memberTravelDoc = isMemberTravelDoc;
	}

}
