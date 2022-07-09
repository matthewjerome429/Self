package com.cathaypacific.mmbbizrule.model.detail.verifier;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.function.Consumer;

import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;
import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageDetail;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BaggageDetailVerifier extends Verifier<BaggageDetail> {

	private BaggageDetailVerifier() {

	}

	public static interface BaggageDetailPropVerifier extends Consumer<BaggageDetail> {

	}

	public static BaggageDetailVerifier expect(BaggageDetailPropVerifier... propVerifiers) {
		BaggageDetailVerifier verifier = new BaggageDetailVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	public static BaggageDetailPropVerifier value(String amount, BaggageUnitEnum unit) {
		return baggageDetail -> {
			assertEquals(new BigInteger(amount), baggageDetail.getAmount());
			assertEquals(unit, baggageDetail.getUnit());
		};
	}

}
