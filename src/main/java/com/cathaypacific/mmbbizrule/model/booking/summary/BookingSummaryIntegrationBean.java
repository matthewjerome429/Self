package com.cathaypacific.mmbbizrule.model.booking.summary;

import java.util.List;

public class BookingSummaryIntegrationBean {
	
	private List<BookingSummary> bookings;
	
	private Boolean retrieveOneASuccess = true;
	
	private Boolean retrieveEODSSuccess = true;
	
	private Boolean retrieveOJSuccess = true;

	public List<BookingSummary> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingSummary> bookings) {
		this.bookings = bookings;
	}

	public Boolean getRetrieveOneASuccess() {
		return retrieveOneASuccess;
	}

	public void setRetrieveOneASuccess(Boolean retrieveOneASuccess) {
		this.retrieveOneASuccess = retrieveOneASuccess;
	}

	public Boolean getRetrieveEODSSuccess() {
		return retrieveEODSSuccess;
	}

	public void setRetrieveEODSSuccess(Boolean retrieveEODSSuccess) {
		this.retrieveEODSSuccess = retrieveEODSSuccess;
	}

	public Boolean getRetrieveOJSuccess() {
		return retrieveOJSuccess;
	}

	public void setRetrieveOJSuccess(Boolean retrieveOJSuccess) {
		this.retrieveOJSuccess = retrieveOJSuccess;
	}
	
	
	
}
