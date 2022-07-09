package com.cathaypacific.mmbbizrule.model.detail.verifier;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.CheckInBaggage;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class CheckInBaggageVerifier extends Verifier<CheckInBaggage> {

	private CheckInBaggageVerifier() {

	}

	public static interface CheckInBaggagePropVerifier extends Consumer<CheckInBaggage> {

	}

	public static CheckInBaggageVerifier expect(CheckInBaggagePropVerifier... propVerifiers) {
		CheckInBaggageVerifier verifier = new CheckInBaggageVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}
	
	public static CheckInBaggagePropVerifier standardBaggage(Verifier<BaggageDetail> standardBaggageVerifier) {
		return checkInBaggage -> standardBaggageVerifier.verify(checkInBaggage.getStandardBaggage());
	}
	
	public static CheckInBaggagePropVerifier waiverBaggage(Verifier<BaggageDetail> waiverBaggageVerifier) {
		return checkInBaggage -> waiverBaggageVerifier.verify(checkInBaggage.getWaiverBaggage());
	}
	
	public static CheckInBaggagePropVerifier memberBaggage(Verifier<BaggageDetail> memberBaggageVerifier) {
		return checkInBaggage -> memberBaggageVerifier.verify(checkInBaggage.getMemberBaggage());
	}
	
	public static CheckInBaggagePropVerifier limit(Verifier<BaggageDetail> limitVerifier) {
		return checkInBaggage -> limitVerifier.verify(checkInBaggage.getLimit());
	}
	
}
