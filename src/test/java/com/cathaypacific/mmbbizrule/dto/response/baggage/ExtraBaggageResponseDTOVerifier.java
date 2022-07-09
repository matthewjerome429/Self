package com.cathaypacific.mmbbizrule.dto.response.baggage;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class ExtraBaggageResponseDTOVerifier extends Verifier<ExtraBaggageResponseDTO> {

	private ExtraBaggageResponseDTOVerifier() {

	}

	public static interface ExtraBaggageResponseDTOPropVerifier extends Consumer<ExtraBaggageResponseDTO> {

	}

	public static ExtraBaggageResponseDTOVerifier expect(ExtraBaggageResponseDTOPropVerifier... propVerifiers) {
		ExtraBaggageResponseDTOVerifier verifier = new ExtraBaggageResponseDTOVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}
	
	@SafeVarargs
	public static ExtraBaggageResponseDTOPropVerifier products(Verifier<BaggageProductDTO>... productsVerifiers) {
		return response -> verifyList(productsVerifiers, response.getProducts());
	}
	
}
