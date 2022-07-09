package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class TravelDocument implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = 378600160552538750L;

	/** Travel Document type */
	private String type;

	/** Travel Document number */
	private String number;

	/** format e.g.2016-12-03 */
	private String expiryDate;

	/** Travel Document issueCountry */
	private String issueCountry;

	/** Travel Document nationality */
	private String nationality;

	/** Passenger Title */
	private String regulatorySurname;

	/** Passenger Title */
	private String regulatoryFirstName;

	/** Passenger Title */
	private String regulatoryGender;
	/** marketing Company, this field only for customerLevel travel document */
	private String marketingCompany;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
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

	public String getRegulatorySurname() {
		return regulatorySurname;
	}

	public void setRegulatorySurname(String regulatorySurname) {
		this.regulatorySurname = regulatorySurname;
	}

	public String getRegulatoryFirstName() {
		return regulatoryFirstName;
	}

	public void setRegulatoryFirstName(String regulatoryFirstName) {
		this.regulatoryFirstName = regulatoryFirstName;
	}

	public String getRegulatoryGender() {
		return regulatoryGender;
	}

	public void setRegulatoryGender(String regulatoryGender) {
		this.regulatoryGender = regulatoryGender;
	}

	public String getMarketingCompany() {
		return marketingCompany;
	}

	public void setMarketingCompany(String marketingCompany) {
		this.marketingCompany = marketingCompany;
	}
}
