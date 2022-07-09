package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od;

import java.io.Serializable;
import java.util.List;

public class BgAlOdResponseDTO implements Serializable {

	private static final long serialVersionUID = 7934462774424149359L;

	private List<BgAlOdBookingResponseDTO> bookings;

	public List<BgAlOdBookingResponseDTO> getBookings() {
		return bookings;
	}

	public void setBookings(List<BgAlOdBookingResponseDTO> bookings) {
		this.bookings = bookings;
	}

}
