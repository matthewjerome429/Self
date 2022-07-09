package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.btu;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BgAlBtuRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BtuRequestDTO;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BgAlBtuRequestDTOVerifier extends Verifier<BgAlBtuRequestDTO> {

	private BgAlBtuRequestDTOVerifier() {

	}

	public static interface BgAlBtuRequestDTOPropVerifier extends Consumer<BgAlBtuRequestDTO> {

	}

	public static BgAlBtuRequestDTOVerifier expect(BgAlBtuRequestDTOPropVerifier... propVerifiers) {
		BgAlBtuRequestDTOVerifier verifier = new BgAlBtuRequestDTOVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	@SafeVarargs
	public static BgAlBtuRequestDTOPropVerifier btu(Verifier<BtuRequestDTO>... btuVerifiers) {
		return requestDTO -> verifyList(btuVerifiers, requestDTO.getBtu());
	}

}
