package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od;

import java.io.Serializable;
import java.util.List;

public class BgAlOdRequestDTO implements Serializable {

	private static final long serialVersionUID = 1034601131593969519L;

	private List<BgAlOdBookingRequestDTO> bookings;

	public List<BgAlOdBookingRequestDTO> getBookings() {
		return bookings;
	}

	public void setBookings(List<BgAlOdBookingRequestDTO> bookings) {
		this.bookings = bookings;
	}

}
