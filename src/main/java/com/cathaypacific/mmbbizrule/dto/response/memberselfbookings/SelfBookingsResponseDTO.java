package com.cathaypacific.mmbbizrule.dto.response.memberselfbookings;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.BookingCustomizedInfoResponseDTO;

public class SelfBookingsResponseDTO extends BaseResponseDTO{

	private static final long serialVersionUID = 6015707630312138804L;
	
	private List<BookingCustomizedInfoResponseDTO> bookings;

	public List<BookingCustomizedInfoResponseDTO> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingCustomizedInfoResponseDTO> bookings) {
		this.bookings = bookings;
	}
	
	
}
