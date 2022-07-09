package com.cathaypacific.mmbbizrule.model.detail.verifier;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageAllowance;
import com.cathaypacific.mmbbizrule.model.booking.detail.CabinBaggage;
import com.cathaypacific.mmbbizrule.model.booking.detail.CheckInBaggage;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BaggageAllowanceVerifier extends Verifier<BaggageAllowance> {

	private BaggageAllowanceVerifier() {

	}

	public static interface BaggageAllowancePropVerifier extends Consumer<BaggageAllowance> {

	}

	public static BaggageAllowanceVerifier expect(BaggageAllowancePropVerifier... propVerifiers) {
		BaggageAllowanceVerifier verifier = new BaggageAllowanceVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	public static BaggageAllowancePropVerifier checkInBaggage(Verifier<CheckInBaggage> checkInBaggageVerifier) {
		return baggageAllowance -> checkInBaggageVerifier.verify(baggageAllowance.getCheckInBaggage());
	}

	public static BaggageAllowancePropVerifier cabinBaggage(Verifier<CabinBaggage> cabinBaggageVerifier) {
		return baggageAllowance -> cabinBaggageVerifier.verify(baggageAllowance.getCabinBaggage());
	}

}
