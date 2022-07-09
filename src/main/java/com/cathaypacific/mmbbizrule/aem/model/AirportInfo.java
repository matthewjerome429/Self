package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirportInfo implements Serializable {

	private static final long serialVersionUID = -6551202727228627944L;

	private String airportCode;

	private Airport airport;

	private List<AEMErrorInfo> errors;

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public Airport getAirport() {
		return airport;
	}

	public void setAirport(Airport airport) {
		this.airport = airport;
	}

	public List<AEMErrorInfo> getErrors() {
		return errors;
	}

	public void setErrors(List<AEMErrorInfo> errors) {
		this.errors = errors;
	}

}
