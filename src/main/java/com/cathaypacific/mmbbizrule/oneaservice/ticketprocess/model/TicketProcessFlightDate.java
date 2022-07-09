package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model;

public class TicketProcessFlightDate {
	
	/** format DDMMYY */
	private String departureDate;
	/** format HHMM */
	private String departureTime;
	/** format DDMMYY */
	private String arrivalDate;
	/** format HHMM */
	private String arrivalTime;

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
}
