package com.cathaypacific.mmbbizrule.cxservice.oj.model.build;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.oj.model.NameInput;

public class OJBooking {
	
	private String bookingReference;

	private String bookingType;
	
	private OJFlightBooking flightBooking;
	
	private OJHotelBooking hotelBooking;
	
	private OJEventBooking eventBooking;
	
	private List<OJDocument> documents;
	
	private String bookingDate;
	
	private OJContactDetails contactDetails;

	private NameInput nameInput;
	
	private String bookingStatus;
	
	public OJBooking() {
		super();
	}

	public OJBooking(String bookingType) {
		super();
		this.bookingType = bookingType;
	}

	public String getBookingReference() {
		return bookingReference;
	}

	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public OJFlightBooking getFlightBooking() {
		return flightBooking;
	}

	public void setFlightBooking(OJFlightBooking flightBooking) {
		this.flightBooking = flightBooking;
	}

	public OJHotelBooking getHotelBooking() {
		return hotelBooking;
	}

	public void setHotelBooking(OJHotelBooking hotelBooking) {
		this.hotelBooking = hotelBooking;
	}

	public OJEventBooking getEventBooking() {
		return eventBooking;
	}

	public void setEventBooking(OJEventBooking eventBooking) {
		this.eventBooking = eventBooking;
	}

	public List<OJDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<OJDocument> documents) {
		this.documents = documents;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public OJContactDetails getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(OJContactDetails contactDetails) {
		this.contactDetails = contactDetails;
	}

	public NameInput getNameInput() {
		return nameInput;
	}

	public void setNameInput(NameInput nameInput) {
		this.nameInput = nameInput;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	
}
