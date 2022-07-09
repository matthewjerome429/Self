package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author tommy.shuai.wang
 *
 */
public class TravelDocDTOV2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8316541939284251922L;
	/** The familyName. */
	private String familyName;
	/** The givenName. */
	private String givenName;
	/** The travelDocumentType. */
	private String travelDocumentType;
	/** The travelDocumentNumber. */
	private String travelDocumentNumber;
	/** The travelDocumentNumber is masked */
	private Boolean docNumberMasked;
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
	/** The birthDateYear is masked */
	private Boolean birthDateYearMasked;
	/** The birthDateMonth. */
	private String birthDateMonth;
	/** The birthDateDay. */
	private String birthDateDay;
	/** The birthDateYear is masked */
	private Boolean birthDateDayMasked;
	/** The company ID. */
	private String companyId;
	/** determine wether this is primary travel doc */
	private Boolean isPrimary;
	/** The gender */
	private String gender;
	/** determine wether this is infant travel doc */
	private Boolean isInfant;

	private List<String> apiVersions;

	private BigInteger qualifierId;
	
	private Boolean productLevel;

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

	public Boolean getDocNumberMasked() {
		return docNumberMasked;
	}

	public void setDocNumberMasked(Boolean docNumberMasked) {
		this.docNumberMasked = docNumberMasked;
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

	public String getBirthDateYear() {
		return birthDateYear;
	}

	public void setBirthDateYear(String birthDateYear) {
		this.birthDateYear = birthDateYear;
	}

	public Boolean getBirthDateYearMasked() {
		return birthDateYearMasked;
	}

	public void setBirthDateYearMasked(Boolean birthDateYearMasked) {
		this.birthDateYearMasked = birthDateYearMasked;
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

	public Boolean getBirthDateDayMasked() {
		return birthDateDayMasked;
	}

	public void setBirthDateDayMasked(Boolean birthDateDayMasked) {
		this.birthDateDayMasked = birthDateDayMasked;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Boolean isPrimary() {
		return isPrimary;
	}

	public void setPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean isInfant() {
		return isInfant;
	}

	public void setInfant(Boolean isInfant) {
		this.isInfant = isInfant;
	}

	public List<String> getApiVersions() {
		return apiVersions;
	}

	public void setApiVersions(List<String> apiVersions) {
		this.apiVersions = apiVersions;
	}

	public BigInteger getQualifierId() {
		return qualifierId;
	}

	public void setQualifierId(BigInteger qualifierId) {
		this.qualifierId = qualifierId;
	}
	
	public Boolean isProductLevel() {
		return productLevel;
	}

	public void setProductLevel(Boolean productLevel) {
		this.productLevel = productLevel;
	}

	@JsonIgnore
	public Boolean isEmpty() {
		return (StringUtils.isEmpty(travelDocumentNumber) && StringUtils.isEmpty(travelDocumentType) && 
				StringUtils.isEmpty(gender) && StringUtils.isEmpty(familyName) && StringUtils.isEmpty(givenName))?true:false;
	}
}
