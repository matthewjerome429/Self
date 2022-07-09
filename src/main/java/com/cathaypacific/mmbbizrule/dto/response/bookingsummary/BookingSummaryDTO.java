package com.cathaypacific.mmbbizrule.dto.response.bookingsummary;

import java.io.Serializable;

public class BookingSummaryDTO implements Serializable {

	private static final long serialVersionUID = 6470439385113961801L;
	
	//TODO will add @JsonIgnore here
	private String rloc;
	
	private String bookingType;
	
	private String booingStatus;
	
	private boolean tempLinkedBooking;
	
	private FlightBookingSummaryDTO flightSummary;
	
	private HotelBookingSummaryDTO hotelSummary;
	
	private EventBookingSummaryDTO eventSummary;
	
	public String getOjRloc() {
		if(flightSummary != null) {
			return flightSummary.getSpnr();
		} else {
			return rloc;
		}
	}

	public String getOneARloc() {
		if(flightSummary != null) {
			return flightSummary.getOneARloc();
		} else {
			return null;
		}
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public String getBooingStatus() {
		return booingStatus;
	}

	public void setBooingStatus(String booingStatus) {
		this.booingStatus = booingStatus;
	}

	public FlightBookingSummaryDTO getFlightSummary() {
		return flightSummary;
	}

	public void setFlightSummary(FlightBookingSummaryDTO flightSummary) {
		this.flightSummary = flightSummary;
	}

	public HotelBookingSummaryDTO getHotelSummary() {
		return hotelSummary;
	}

	public void setHotelSummary(HotelBookingSummaryDTO hotelSummary) {
		this.hotelSummary = hotelSummary;
	}

	public EventBookingSummaryDTO getEventSummary() {
		return eventSummary;
	}

	public void setEventSummary(EventBookingSummaryDTO eventSummary) {
		this.eventSummary = eventSummary;
	}

	public boolean isTempLinkedBooking() {
		return tempLinkedBooking;
	}

	public void setTempLinkedBooking(boolean tempLinkedBooking) {
		this.tempLinkedBooking = tempLinkedBooking;
	}
	
}
