package com.cathaypacific.mmbbizrule.cxservice.plusgrade.model;

import java.io.Serializable;

public class PlusgradeRequestTicketDTO implements Serializable {
	
	private static final long serialVersionUID = 3972299362672798053L;
	
	private String firstName;
	private String lastName;
	private Boolean primaryTraveller;
	private String ticketNumber;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Boolean getPrimaryTraveller() {
		return primaryTraveller;
	}
	public void setPrimaryTraveller(Boolean primaryTraveller) {
		this.primaryTraveller = primaryTraveller;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	
}
