package com.cathaypacific.mmbbizrule.model.common.transit;

import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
/**
 * This bean use for booking model cache just
 * @author zilong.bu
 *
 */
public class BookingCache {

	private Booking booking;
	
	private BookingBuildRequired required;

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public BookingBuildRequired getRequired() {
		return required;
	}

	public void setRequired(BookingBuildRequired required) {
		this.required = required;
	}
	
}
