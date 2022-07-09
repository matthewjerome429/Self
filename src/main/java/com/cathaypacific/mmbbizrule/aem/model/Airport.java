package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;

public class Airport implements Serializable {
	
	private static final long serialVersionUID = -66234333200272903L;

	private String airportCode;
	
	private String airportFullName;
	
	private String airportShortName;
	
	private City city;
	
	private Country country;

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public String getAirportFullName() {
		return airportFullName;
	}

	public void setAirportFullName(String airportFullName) {
		this.airportFullName = airportFullName;
	}

	public String getAirportShortName() {
		return airportShortName;
	}

	public void setAirportShortName(String airportShortName) {
		this.airportShortName = airportShortName;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
}
