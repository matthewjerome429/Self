package com.cathaypacific.mmbbizrule.cxservice.eods.model;

import java.util.List;

public class EODSBooking {

	private String rloc;

	private String bookingType;
	
	private List<EODSBookingPassenger> passengers;

	private List<EODSBookingSegment> segments;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<EODSBookingPassenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<EODSBookingPassenger> passengers) {
		this.passengers = passengers;
	}

	public List<EODSBookingSegment> getSegments() {
		return segments;
	}

	public void setSegments(List<EODSBookingSegment> segments) {
		this.segments = segments;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

}
