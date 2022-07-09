package com.cathaypacific.mmbbizrule.cxservice.oj.model;

public class OJBookingRequestDTO {
	private String reference;
	
	private String firstName;
	
	private String surName;

	/* For member login flow - TODO
	 * private String memberID;
	
	private String mlcToken;*/
	
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}
	
}
