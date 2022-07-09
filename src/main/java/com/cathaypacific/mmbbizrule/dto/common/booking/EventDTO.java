package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;
import java.util.List;

public class EventDTO extends BookingOrderDTO implements Serializable {
	
	private static final long serialVersionUID = 2247471002865138526L;

	private String reference;
	
	private String bookingReference;
	
	private String bookingStatus;
	
	private String date;
	
	private String time;
	
	private String name;
	
	private EventDescriptionDTO description;
	
	private List<EventGuestDTO> guests;

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

	public EventDescriptionDTO getDescription() {
		return description;
	}

	public void setDescription(EventDescriptionDTO description) {
		this.description = description;
	}

	public List<EventGuestDTO> getGuests() {
		return guests;
	}

	public void setGuests(List<EventGuestDTO> guests) {
		this.guests = guests;
	}
	
}
