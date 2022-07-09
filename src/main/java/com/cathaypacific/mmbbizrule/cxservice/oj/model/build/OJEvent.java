package com.cathaypacific.mmbbizrule.cxservice.oj.model.build;

import java.util.List;

public class OJEvent extends OJBookingOrder {
	
	/** Format of date*/
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/** Format of time*/
	public static final String TIME_FORMAT = "HH:mm";

	private String reference;
	
	private String bookingReference;
	
	private String bookingStatus;
	
	private String date;
	
	private String time;
	
	private String name;
	
	private OJEventDescription description;
	
	private List<OJEventGuest> guests;
	
	private String airportCode;
	
	private String timeZoneOffset;
	
	private String cityName;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getBookingReference() {
		return bookingReference;
	}

	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OJEventDescription getDescription() {
		return description;
	}

	public void setDescription(OJEventDescription description) {
		this.description = description;
	}

	public List<OJEventGuest> getGuests() {
		return guests;
	}

	public void setGuests(List<OJEventGuest> guests) {
		this.guests = guests;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
