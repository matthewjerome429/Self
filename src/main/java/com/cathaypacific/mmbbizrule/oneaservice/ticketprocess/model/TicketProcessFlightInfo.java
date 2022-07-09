package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model;

public class TicketProcessFlightInfo {
	
	private TicketProcessFlightDate flightDate;
	
	private String boardPoint;
	
	private String offpoint;
	
	private String marketingCompany;
	
	private String operatingCompany;
	
	private String flightNumber;
	
	private String bookingClass;

	public TicketProcessFlightDate getFlightDate() {
		return flightDate;
	}
	
	public TicketProcessFlightDate findFlightDate() {
		if(flightDate == null){
			flightDate = new TicketProcessFlightDate();
		}
		return flightDate;
	}

	public void setFlightDate(TicketProcessFlightDate flightDate) {
		this.flightDate = flightDate;
	}

	public String getBoardPoint() {
		return boardPoint;
	}

	public void setBoardPoint(String boardPoint) {
		this.boardPoint = boardPoint;
	}

	public String getOffpoint() {
		return offpoint;
	}

	public void setOffpoint(String offpoint) {
		this.offpoint = offpoint;
	}

	public String getMarketingCompany() {
		return marketingCompany;
	}

	public void setMarketingCompany(String marketingCompany) {
		this.marketingCompany = marketingCompany;
	}

	public String getOperatingCompany() {
		return operatingCompany;
	}

	public void setOperatingCompany(String operatingCompany) {
		this.operatingCompany = operatingCompany;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getBookingClass() {
		return bookingClass;
	}

	public void setBookingClass(String bookingClass) {
		this.bookingClass = bookingClass;
	}
}
