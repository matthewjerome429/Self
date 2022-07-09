package com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking;

import com.cathaypacific.mmbbizrule.v2.dto.common.booking.BookingDTOV2;

public class RefreshBookingResponseDTOV2 extends RetrieveBookingBaseResponseDTOV2{
	
	private static final long serialVersionUID = 3953975433097519973L;

	private BookingDTOV2 booking;

	public BookingDTOV2 getBooking() {
		return booking;
	}

	public void setBooking(BookingDTOV2 booking) {
		this.booking = booking;
	}

}
