package com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking;

import java.util.List;

import com.cathaypacific.mmbbizrule.dto.common.booking.BookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;

import io.swagger.annotations.ApiModel;
@ApiModel
public class ReceivePnrByRlocForMemberResponseDTOV2 extends RetrieveBookingBaseResponseDTOV2 {

	private static final long serialVersionUID = 2864929952836601630L;

	private FlightBookingDTO booking;
	
	private List<BookingDTO> bookingList;
	
	public FlightBookingDTO getBooking() {
		return booking;
	}

	public void setBooking(FlightBookingDTO booking) {
		this.booking = booking;
	}

	public List<BookingDTO> getBookingList() {
		return bookingList;
	}

	public void setBookingList(List<BookingDTO> bookingList) {
		this.bookingList = bookingList;
	}
	
}
