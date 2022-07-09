package com.cathaypacific.mmbbizrule.model.detail.verifier;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.CabinBaggage;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class CabinBaggageVerifier extends Verifier<CabinBaggage> {

	private CabinBaggageVerifier() {

	}

	public static interface CabinBaggagePropVerifier extends Consumer<CabinBaggage> {

	}

	public static CabinBaggageVerifier expect(CabinBaggagePropVerifier... propVerifiers) {
		CabinBaggageVerifier verifier = new CabinBaggageVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	public static CabinBaggagePropVerifier standardBaggage(Verifier<BaggageDetail> standardBaggageVerifier) {
		return cabinBaggage -> standardBaggageVerifier.verify(cabinBaggage.getStandardBaggage());
	}

	public static CabinBaggagePropVerifier memberBaggage(Verifier<BaggageDetail> memberBaggageVerifier) {
		return cabinBaggage -> memberBaggageVerifier.verify(cabinBaggage.getMemberBaggage());
	}

	public static CabinBaggagePropVerifier limit(Verifier<BaggageDetail> limitVerifier) {
		return cabinBaggage -> limitVerifier.verify(cabinBaggage.getLimit());
	}

	public static CabinBaggagePropVerifier total(Verifier<BaggageDetail> totalVerifier) {
		return cabinBaggage -> totalVerifier.verify(cabinBaggage.getTotal());
	}

}
