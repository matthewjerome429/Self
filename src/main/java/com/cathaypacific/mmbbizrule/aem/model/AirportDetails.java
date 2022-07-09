package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;

public class AirportDetails implements Serializable {

	private static final long serialVersionUID = 1698372827892150271L;

	private String airportFullName;
	
	private String airportShortName;
	
	private String defaultAirportFullName;
	
	private String defaultAirportShortName;
	
	private City city;
	
	private Country country;

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

	public String getDefaultAirportFullName() {
		return defaultAirportFullName;
	}

	public void setDefaultAirportFullName(String defaultAirportFullName) {
		this.defaultAirportFullName = defaultAirportFullName;
	}

	public String getDefaultAirportShortName() {
		return defaultAirportShortName;
	}

	public void setDefaultAirportShortName(String defaultAirportShortName) {
		this.defaultAirportShortName = defaultAirportShortName;
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
