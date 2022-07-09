package com.cathaypacific.mmbbizrule.model.detail.verifier;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class PassengerVerifier extends Verifier<Passenger> {

	private PassengerVerifier() {

	}

	public static interface PassengerPropVerifier extends Consumer<Passenger> {

	}

	public static PassengerVerifier expect(PassengerPropVerifier... propVerifiers) {
		PassengerVerifier verifier = new PassengerVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	public static PassengerPropVerifier passengerID(String passengerID) {
		return passenger -> assertEquals(passengerID, passenger.getPassengerId());
	}

}
