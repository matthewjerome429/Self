package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.AirportUpgradeInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageAllowance;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;

public class PassengerSegmentMocker {

	private PassengerSegmentMocker() {

	}

	public static interface PassengerSegmentPropMocker extends Consumer<PassengerSegment> {

	}

	public static PassengerSegment mock(PassengerSegmentPropMocker... propMockers) {
		PassengerSegment obj = new PassengerSegment();

		for (PassengerSegmentPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static PassengerSegmentPropMocker passengerSegmentID(String passengerID, String segmentID) {
		return passengerSegment -> {
			passengerSegment.setPassengerId(passengerID);
			passengerSegment.setSegmentId(segmentID);
		};
	}
	
	public static PassengerSegmentPropMocker baggageAllowance(BaggageAllowance baggageAllowance) {
		return passengerSegment -> passengerSegment.setBaggageAllowance(baggageAllowance);
	}
	
	public static PassengerSegmentPropMocker fqtvInfo(FQTVInfo fqtvInfo) {
		return passengerSegment -> passengerSegment.setFqtvInfo(fqtvInfo);
	}
	
	public static PassengerSegmentPropMocker airportUpgradeInfo(AirportUpgradeInfo airportUpgradeInfo) {
		return passengerSegment -> passengerSegment.setAirportUpgradeInfo(airportUpgradeInfo);
	}
	
}
