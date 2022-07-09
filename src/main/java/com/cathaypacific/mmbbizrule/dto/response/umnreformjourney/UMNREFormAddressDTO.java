package com.cathaypacific.mmbbizrule.dto.response.umnreformjourney;

import org.apache.commons.lang.StringUtils;

public class UMNREFormAddressDTO {

	private String building;
	
	private String street;
	
	private String city;
	
	private String countryCode;
	
	private String defaultCountryName;

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

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
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDefaultCountryName() {
		return defaultCountryName;
	}

	public void setDefaultCountryName(String defaultCountryName) {
		if (!StringUtils.isEmpty(defaultCountryName)) {
			this.defaultCountryName = defaultCountryName.toUpperCase();
		}
	}

	public String toString() {
		StringBuilder addressBuilder = new StringBuilder();
		if (!StringUtils.isEmpty(building)) {
			addressBuilder.append(building + " ");
		}
		if (!StringUtils.isEmpty(street)) {
			addressBuilder.append(street + " ");
		}
		if (!StringUtils.isEmpty(city)) {
			addressBuilder.append(city + " ");
		}
		if (!StringUtils.isEmpty(defaultCountryName)) {
			addressBuilder.append(defaultCountryName + " ");
		}
		return addressBuilder.toString();
	}
	
	public String toBuildStreetString() {
		StringBuilder addressBuilder = new StringBuilder();
		if (!StringUtils.isEmpty(building)) {
			addressBuilder.append(building + " ");
		}
		if (!StringUtils.isEmpty(street)) {
			addressBuilder.append(street + " ");
		}
		return addressBuilder.toString();
	}
	
	public String toCityCountryString() {
		StringBuilder addressBuilder = new StringBuilder();
		if (!StringUtils.isEmpty(city)) {
			addressBuilder.append(city + " ");
		}
		if (!StringUtils.isEmpty(defaultCountryName)) {
			addressBuilder.append(defaultCountryName + " ");
		}
		return addressBuilder.toString();
	}
	
}
