package com.cathaypacific.mmbbizrule.dto.response.bookingsummary;

public class EventSummaryDTO extends SummaryDTO {

	private static final long serialVersionUID = 6458997743578385311L;

	private String bookingStatus;
	
	private String date;
	
	private String time;
	
	private String name;

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
	
}
