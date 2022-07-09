package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;

public class PassengerMocker {

	private PassengerMocker() {

	}

	public static interface PassengerPropMocker extends Consumer<Passenger> {

	}

	public static Passenger mock(PassengerPropMocker... propMockers) {
		Passenger obj = new Passenger();

		for (PassengerPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static PassengerPropMocker passengerID(String passengerID) {
		return passenger -> passenger.setPassengerId(passengerID);
	}
	
}
