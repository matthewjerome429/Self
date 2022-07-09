package com.cathaypacific.mmbbizrule.dto.response.baggage;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BaggageProductValueDTOVerifier extends Verifier<BaggageProductValueDTO> {

	private BaggageProductValueDTOVerifier() {

	}

	public static interface BaggageProductValueDTOPropVerifier extends Consumer<BaggageProductValueDTO> {

	}

	public static BaggageProductValueDTOVerifier expect(BaggageProductValueDTOPropVerifier... propVerifiers) {
		BaggageProductValueDTOVerifier verifier = new BaggageProductValueDTOVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}
	
	public static BaggageProductValueDTOPropVerifier value(String value) {
		return productValue -> assertEquals(value, productValue.getValue());
	}
	
	public static BaggageProductValueDTOPropVerifier amount(String amount) {
		return productValue -> assertEquals(amount, productValue.getAmount());
	}
	
	public static BaggageProductValueDTOPropVerifier currency(String currency) {
		return productValue -> assertEquals(currency, productValue.getCurrency());
	}
	
}
