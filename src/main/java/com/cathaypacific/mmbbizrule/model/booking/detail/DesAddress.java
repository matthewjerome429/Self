package com.cathaypacific.mmbbizrule.model.booking.detail;

import org.apache.commons.lang.StringUtils;

public class DesAddress {
	/** Street name */
	private String street;
	/** city */
	private String city;
	/** State code */
	private String stateCode;
	/** Zip code */
	private String zipCode;
	
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
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(street) && StringUtils.isEmpty(city) && StringUtils.isEmpty(stateCode) && StringUtils.isEmpty(zipCode);
	}
}
