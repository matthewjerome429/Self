package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class EmergencyContact implements Serializable {

	private static final long serialVersionUID = 5098261995688111233L;

	private String countryCode;

	private String surname;

	private String phoneNumber;

	private String firstName;

	private String marketingCompany;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMarketingCompany() {
		return marketingCompany;
	}

	public void setMarketingCompany(String marketingCompany) {
		this.marketingCompany = marketingCompany;
	}
}
