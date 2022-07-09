package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TravelDocumentRecord {

    private String documentNumber;

   // @JsonFormat(pattern="yyyy-MM-dd")
    private String expiryDate;

    private String familyName;

    private String givenName;

    private String issueCountry;

    private String nationality;

    private String type;

    public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(final String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(final String givenName) {
        this.givenName = givenName;
    }

    public String getIssueCountry() {
        return issueCountry;
    }

    public void setIssueCountry(final String issueCountry) {
        this.issueCountry = issueCountry;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(final String nationality) {
        this.nationality = nationality;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
    
}
