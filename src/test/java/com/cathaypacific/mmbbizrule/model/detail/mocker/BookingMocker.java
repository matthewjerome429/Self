package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;

public class BookingMocker {

	private BookingMocker() {

	}

	public static interface BookingPropMocker extends Consumer<Booking> {

	}

	public static Booking mock(BookingPropMocker... propMockers) {
		Booking obj = new Booking();

		for (BookingPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static BookingPropMocker oneARloc(String oneARloc) {
		return booking -> booking.setOneARloc(oneARloc);
	}
	
	public static BookingPropMocker passengers(Passenger... passengers) {
		return booking -> booking.setPassengers(Arrays.asList(passengers));
	}
	
	public static BookingPropMocker segments(Segment... segments) {
		return booking -> booking.setSegments(Arrays.asList(segments));
	}
	
	public static BookingPropMocker passengerSegments(PassengerSegment... passengerSegments) {
		return booking -> booking.setPassengerSegments(Arrays.asList(passengerSegments));
	}
	
}
