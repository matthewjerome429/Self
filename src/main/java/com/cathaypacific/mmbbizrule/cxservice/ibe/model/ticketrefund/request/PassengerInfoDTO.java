package com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request;

public class PassengerInfoDTO {
	
	private String passengerLastName;
	
	private String passengerFirstName;
	
	private String eTicketNumber;
	
	private String remarks;

	public String getPassengerLastName() {
		return passengerLastName;
	}

	public void setPassengerLastName(String passengerLastName) {
		this.passengerLastName = passengerLastName;
	}

	public String getPassengerFirstName() {
		return passengerFirstName;
	}

	public void setPassengerFirstName(String passengerFirstName) {
		this.passengerFirstName = passengerFirstName;
	}

	public String geteTicketNumber() {
		return eTicketNumber;
	}

	public void seteTicketNumber(String eTicketNumber) {
		this.eTicketNumber = eTicketNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
