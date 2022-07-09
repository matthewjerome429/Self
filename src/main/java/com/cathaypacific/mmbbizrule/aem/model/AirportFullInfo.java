package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;

public class AirportFullInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4801024591162000314L;

	private String airportCode;
	
	private String regionName;
	
	private boolean excludeInfants;
	
	private boolean cotsIncludeChildren;
	
	private boolean cotsIncludeInfants;
	
	private AirportDetails airportDetails;

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public boolean isExcludeInfants() {
		return excludeInfants;
	}

	public void setExcludeInfants(boolean excludeInfants) {
		this.excludeInfants = excludeInfants;
	}

	public boolean isCotsIncludeChildren() {
		return cotsIncludeChildren;
	}

	public void setCotsIncludeChildren(boolean cotsIncludeChildren) {
		this.cotsIncludeChildren = cotsIncludeChildren;
	}

	public boolean isCotsIncludeInfants() {
		return cotsIncludeInfants;
	}

	public void setCotsIncludeInfants(boolean cotsIncludeInfants) {
		this.cotsIncludeInfants = cotsIncludeInfants;
	}

	public AirportDetails getAirportDetails() {
		return airportDetails;
	}

	public void setAirportDetails(AirportDetails airportDetails) {
		this.airportDetails = airportDetails;
	}
	
}
