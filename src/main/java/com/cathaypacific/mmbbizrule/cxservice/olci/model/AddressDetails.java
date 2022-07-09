package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class AddressDetails implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -7445775413374625834L;

	/** Street and number or post office box identifier */
	private String street;

	/** City name */
	private String city;

	/** Country sub-entity code, aka State Province Code */
	private String countrySubEntity;

	/** Country sub-entity name, aka State Province. */
	private String countrySubEntityName;

	/** Postal identification code, aka Zip Code. */
	private String postCode;

	/** Country code */
	private String countryCode;

	/** The marketing company of this address */
	private String marketingCompany;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountrySubEntity() {
		return countrySubEntity;
	}

	public void setCountrySubEntity(String countrySubEntity) {
		this.countrySubEntity = countrySubEntity;
	}

	public String getCountrySubEntityName() {
		return countrySubEntityName;
	}

	public void setCountrySubEntityName(String countrySubEntityName) {
		this.countrySubEntityName = countrySubEntityName;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMarketingCompany() {
		return marketingCompany;
	}

	public void setMarketingCompany(String marketingCompany) {
		this.marketingCompany = marketingCompany;
	}
}
