package com.cathaypacific.mmbbizrule.dto.response.updateseat;

import java.io.Serializable;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;

public class UpdateSeatResponseDTO extends BaseResponseDTO implements Serializable{
	private static final long serialVersionUID = 1190850776755371403L;
	
	private FlightBookingDTO booking;

	public FlightBookingDTO getBooking() {
		return booking;
	}

	public void setBooking(FlightBookingDTO booking) {
		this.booking = booking;
	}
}
