package com.cathaypacific.mmbbizrule.model.detail.verifier;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageAllowance;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class PassengerSegmentVerifier extends Verifier<PassengerSegment> {

	private PassengerSegmentVerifier() {

	}

	public static interface PassengerSegmentPropVerifier extends Consumer<PassengerSegment> {

	}

	public static PassengerSegmentVerifier expect(PassengerSegmentPropVerifier... propVerifiers) {
		PassengerSegmentVerifier verifier = new PassengerSegmentVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}
	
	public static PassengerSegmentPropVerifier passengerSegmentID(String passengerID, String segmentID) {
		return passengerSegment -> {
			assertEquals(passengerID, passengerSegment.getPassengerId());
			assertEquals(passengerSegment.getSegmentId(), segmentID);
		};
	}
	
	public static PassengerSegmentPropVerifier baggageAllowance(Verifier<BaggageAllowance> baggageAllowanceVerifier) {
		return passengerSegment -> baggageAllowanceVerifier.verify(passengerSegment.getBaggageAllowance());
	}
	
}
