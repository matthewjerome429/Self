package com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class BookingSummaryResponseDTOV2 extends BaseResponseDTO{

	private static final long serialVersionUID = 8743147067468360427L;

	private int bookingCount;
	
	private List<BookingSummaryDTOV2> bookings;
	
	private Boolean retrieveOneASuccess = true;
	
	private Boolean retrieveEODSSuccess = true;
	
	private Boolean retrieveOJSuccess = true;
	
 

	public List<BookingSummaryDTOV2> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingSummaryDTOV2> bookings) {
		this.bookings = bookings;
	}

	public int getBookingCount() {
		return bookingCount;
	}

	public void setBookingCount(int bookingCount) {
		this.bookingCount = bookingCount;
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
