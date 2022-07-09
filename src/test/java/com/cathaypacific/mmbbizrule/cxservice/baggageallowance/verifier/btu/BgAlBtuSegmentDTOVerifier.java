package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.btu;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BgAlBtuSegmentDTO;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BgAlBtuSegmentDTOVerifier extends Verifier<BgAlBtuSegmentDTO> {

	private BgAlBtuSegmentDTOVerifier() {

	}

	public static interface BgAlBtuSegmentDTOPropVerifier extends Consumer<BgAlBtuSegmentDTO> {

	}

	public static BgAlBtuSegmentDTOVerifier expect(BgAlBtuSegmentDTOPropVerifier... propVerifiers) {
		BgAlBtuSegmentDTOVerifier verifier = new BgAlBtuSegmentDTOVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	public static BgAlBtuSegmentDTOPropVerifier point(String boardPoint, String offPoint) {
		return segmentDTO -> {
			assertEquals(boardPoint, segmentDTO.getBoardPoint());
			assertEquals(offPoint, segmentDTO.getOffPoint());
		};
	}

	public static BgAlBtuSegmentDTOPropVerifier operatingCarrier(String operatingCarrier) {
		return segmentDTO -> assertEquals(operatingCarrier, segmentDTO.getOperatingCarrier());
	}

	public static BgAlBtuSegmentDTOPropVerifier cabinClass(String cabinClass) {
		return segmentDTO -> assertEquals(cabinClass, segmentDTO.getCabinClass());
	}

	public static BgAlBtuSegmentDTOPropVerifier departure(String departureDate, String departureTime) {
		return segmentDTO -> {
			assertEquals(departureDate, segmentDTO.getDepartureDate());
			assertEquals(departureTime, segmentDTO.getDepartureTime());
		};
	}

}
