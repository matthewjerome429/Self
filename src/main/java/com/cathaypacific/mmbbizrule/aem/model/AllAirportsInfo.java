package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;
import java.util.List;

public class AllAirportsInfo implements Serializable {

	private static final long serialVersionUID = 5913352229007096114L;

	private List<AirportFullInfo> airports;
	
	private List<AEMErrorInfo> errors;

	public List<AirportFullInfo> getAirports() {
		return airports;
	}

	public void setAirports(List<AirportFullInfo> airports) {
		this.airports = airports;
	}

	public List<AEMErrorInfo> getErrors() {
		return errors;
	}

	public void setErrors(List<AEMErrorInfo> errors) {
		this.errors = errors;
	}
	
	
}
