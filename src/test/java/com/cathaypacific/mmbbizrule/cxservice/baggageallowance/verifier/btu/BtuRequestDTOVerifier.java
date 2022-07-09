package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.btu;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BgAlBtuSegmentDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BtuRequestDTO;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BtuRequestDTOVerifier extends Verifier<BtuRequestDTO> {

	private BtuRequestDTOVerifier() {

	}

	public static interface BtuRequestDTOPropVerifier extends Consumer<BtuRequestDTO> {

	}

	public static BtuRequestDTOVerifier expect(BtuRequestDTOPropVerifier... propVerifiers) {
		BtuRequestDTOVerifier verifier = new BtuRequestDTOVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	public static BtuRequestDTOPropVerifier btuId(String btuId) {
		return btuDTO -> assertEquals(btuId, btuDTO.getBtuId());
	}

	public static BtuRequestDTOPropVerifier baggageAllowanceType(String baggageAllowanceType) {
		return btuDTO -> assertEquals(baggageAllowanceType, btuDTO.getBaggageAllowanceType());
	}

	@SafeVarargs
	public static BtuRequestDTOPropVerifier segments(Verifier<BgAlBtuSegmentDTO>... segmentVerifiers) {
		return btuDTO -> verifyList(segmentVerifiers, btuDTO.getSegments());
	}

}
