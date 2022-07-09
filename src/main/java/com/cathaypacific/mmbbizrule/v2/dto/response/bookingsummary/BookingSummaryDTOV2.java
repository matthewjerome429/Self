package com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class BookingSummaryDTOV2 implements Serializable {

	private static final long serialVersionUID = 6470439385113961801L;
	
	//TODO will add @JsonIgnore here
	@Deprecated
	@ApiModelProperty(value = "Same as displayRloc, please don't use this field anymore, will remove it this version later", required = true, example = "MX4768")
	private String rloc;
	
	private String displayRloc;
	
	private String spnr;
	
	private String bookingType;
	
	private String booingStatus;
	
	private boolean tempLinkedBooking;
	
	private FlightBookingSummaryDTOV2 flightSummary;
	
	private HotelBookingSummaryDTOV2 hotelSummary;
	
	private EventBookingSummaryDTOV2 eventSummary;
	@Deprecated
	@ApiModelProperty(value = "Same as spnr, please don't use this field anymore, will remove it this version later", required = true, example = "MX4768X")
	public String getOjRloc() {
		if(flightSummary != null) {
			return flightSummary.getSpnr();
		} else {
			return spnr;
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
		return displayRloc;
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

	public FlightBookingSummaryDTOV2 getFlightSummary() {
		return flightSummary;
	}

	public void setFlightSummary(FlightBookingSummaryDTOV2 flightSummary) {
		this.flightSummary = flightSummary;
	}

	public HotelBookingSummaryDTOV2 getHotelSummary() {
		return hotelSummary;
	}

	public void setHotelSummary(HotelBookingSummaryDTOV2 hotelSummary) {
		this.hotelSummary = hotelSummary;
	}

	public EventBookingSummaryDTOV2 getEventSummary() {
		return eventSummary;
	}

	public void setEventSummary(EventBookingSummaryDTOV2 eventSummary) {
		this.eventSummary = eventSummary;
	}

	public boolean isTempLinkedBooking() {
		return tempLinkedBooking;
	}

	public void setTempLinkedBooking(boolean tempLinkedBooking) {
		this.tempLinkedBooking = tempLinkedBooking;
	}

	public String getDisplayRloc() {
		return displayRloc;
	}

	public void setDisplayRloc(String displayRloc) {
		this.displayRloc = displayRloc;
	}

	public String getSpnr() {
		return spnr;
	}

	public void setSpnr(String spnr) {
		this.spnr = spnr;
	}
	
}
