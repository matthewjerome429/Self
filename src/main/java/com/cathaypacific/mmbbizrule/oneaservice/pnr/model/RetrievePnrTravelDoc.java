package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

public class RetrievePnrTravelDoc {
	/** The familyName. */
    private String familyName;
    /** The givenName. */
    private String givenName;
    /** The travelDocumentType. */
	private String travelDocumentType;
    /** The travelDocumentNumber. */
    private String travelDocumentNumber;
    /** The countryOfIssuance */
    private String countryOfIssuance;
    /** The nationality. */
    private String nationality;
    /** The expiryDateYear. */
    private String expiryDateYear;
    /** The middle name. */
    private String expiryDateMonth;
    /** The expiryDateDay. */
    private String expiryDateDay;
    /** The birthDateYear. */
    private String birthDateYear;
    /** The birthDateMonth. */
    private String birthDateMonth;
    /** The birthDateDay. */
    private String birthDateDay;
    /** The company ID. */
    private String companyId;
    /** The gender*/
    private String gender;
    /** determine wether this is infant travel doc*/
    private boolean isInfant;
    /** ssrType for this TravelDoc. eg. DOCS DOCO*/
    private String ssrType;
    /** qualifier ID */
    private BigInteger qualifierId;
    /** To store extraFreeText. eg. first Field in DOCO freeText */
    private String extraFreeText;
    
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
	public String getTravelDocumentType() {
		return travelDocumentType;
	}
	public void setTravelDocumentType(String travelDocumentType) {
		this.travelDocumentType = travelDocumentType;
	}
	public String getTravelDocumentNumber() {
		return travelDocumentNumber;
	}
	public void setTravelDocumentNumber(String travelDocumentNumber) {
		this.travelDocumentNumber = travelDocumentNumber;
	}
	public String getCountryOfIssuance() {
		return countryOfIssuance;
	}
	public void setCountryOfIssuance(String countryOfIssuance) {
		this.countryOfIssuance = countryOfIssuance;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getExpiryDateYear() {
		return expiryDateYear;
	}
	public void setExpiryDateYear(String expiryDateYear) {
		this.expiryDateYear = expiryDateYear;
	}
	public String getExpiryDateMonth() {
		return expiryDateMonth;
	}
	public void setExpiryDateMonth(String expiryDateMonth) {
		this.expiryDateMonth = expiryDateMonth;
	}
	public String getExpiryDateDay() {
		return expiryDateDay;
	}
	public void setExpiryDateDay(String expiryDateDay) {
		this.expiryDateDay = expiryDateDay;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getBirthDateYear() {
		return birthDateYear;
	}
	public void setBirthDateYear(String birthDateYear) {
		this.birthDateYear = birthDateYear;
	}
	public String getBirthDateMonth() {
		return birthDateMonth;
	}
	public void setBirthDateMonth(String birthDateMonth) {
		this.birthDateMonth = birthDateMonth;
	}
	public String getBirthDateDay() {
		return birthDateDay;
	}
	public void setBirthDateDay(String birthDateDay) {
		this.birthDateDay = birthDateDay;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public boolean isInfant() {
		return isInfant;
	}
	public void setInfant(boolean isInfant) {
		this.isInfant = isInfant;
	}
	public String getSsrType() {
		return ssrType;
	}
	public void setSsrType(String ssrType) {
		this.ssrType = ssrType;
	}
	public BigInteger getQualifierId() {
		return qualifierId;
	}
	public void setQualifierId(BigInteger qualifierId) {
		this.qualifierId = qualifierId;
	}
	public String getExtraFreeText() {
		return extraFreeText;
	}
	public void setExtraFreeText(String extraFreeText) {
		this.extraFreeText = extraFreeText;
	}
	
}
