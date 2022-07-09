package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

public class RetrievePnrEticket {
	
	/** Format of time*/
	public static final String TIME_FORMAT = "ddMMMyy";
	
	private String ticketNumber;
	
	private String lineNumber;
	
	private String date;
	
	private String passengerType;
	
	private String currency;
	
	private String amount;

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
}
