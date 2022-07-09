package com.cathaypacific.mmbbizrule.dto.response.retrievepnr;

import com.cathaypacific.mmbbizrule.dto.common.booking.BookingDTO;

public class RefreshBookingResponseDTO extends RetrieveBookingBaseResponseDTO{
	
	private static final long serialVersionUID = 3953975433097519973L;

	private BookingDTO booking;

	public BookingDTO getBooking() {
		return booking;
	}

	public void setBooking(BookingDTO booking) {
		this.booking = booking;
	}

}
