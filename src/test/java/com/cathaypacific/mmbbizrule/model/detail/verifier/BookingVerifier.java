package com.cathaypacific.mmbbizrule.model.detail.verifier;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BookingVerifier extends Verifier<Booking> {

	private BookingVerifier() {

	}

	public static interface BookingPropVerifier extends Consumer<Booking> {

	}

	public static BookingVerifier expect(BookingPropVerifier... propVerifiers) {
		BookingVerifier verifier = new BookingVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}
	
	@SafeVarargs
	public static BookingPropVerifier passengers(Verifier<Passenger>... passengerVerifiers) {
		return booking -> verifyList(passengerVerifiers, booking.getPassengers());
	}

	@SafeVarargs
	public static BookingPropVerifier segments(Verifier<Segment>... segmentVerifiers) {
		return booking -> verifyList(segmentVerifiers, booking.getSegments());
	}

	@SafeVarargs
	public static BookingPropVerifier passengerSegments(Verifier<PassengerSegment>... passengerSegmentVerifiers) {
		return booking -> verifyList(passengerSegmentVerifiers, booking.getPassengerSegments());
	}

}
