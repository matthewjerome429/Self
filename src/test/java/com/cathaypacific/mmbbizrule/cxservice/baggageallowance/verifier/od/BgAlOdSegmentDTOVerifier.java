package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.od;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.common.od.BgAlOdSegmentDTO;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BgAlOdSegmentDTOVerifier extends Verifier<BgAlOdSegmentDTO> {

	private BgAlOdSegmentDTOVerifier() {

	}

	public static interface BgAlOdSegmentDTOPropVerifier extends Consumer<BgAlOdSegmentDTO> {

	}

	public static BgAlOdSegmentDTOVerifier expect(BgAlOdSegmentDTOPropVerifier... propVerifiers) {
		BgAlOdSegmentDTOVerifier verifier = new BgAlOdSegmentDTOVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	public static BgAlOdSegmentDTOPropVerifier point(String boardPoint, String offPoint) {
		return segmentDTO -> {
			assertEquals(boardPoint, segmentDTO.getBoardPoint());
			assertEquals(offPoint, segmentDTO.getOffPoint());
		};
	}

	public static BgAlOdSegmentDTOPropVerifier operatingCarrier(String operatingCarrier) {
		return segmentDTO -> assertEquals(operatingCarrier, segmentDTO.getOperatingCarrier());
	}

	public static BgAlOdSegmentDTOPropVerifier marketingCarrier(String marketingCarrier) {
		return segmentDTO -> assertEquals(marketingCarrier, segmentDTO.getMarketingCarrier());
	}

	public static BgAlOdSegmentDTOPropVerifier cabinClass(String cabinClass) {
		return segmentDTO -> assertEquals(cabinClass, segmentDTO.getCabinClass());
	}

	public static BgAlOdSegmentDTOPropVerifier departure(String departureDate, String departureTime) {
		return segmentDTO -> {
			assertEquals(departureDate, segmentDTO.getDepartureDate());
			assertEquals(departureTime, segmentDTO.getDepartureTime());
		};
	}

	public static BgAlOdSegmentDTOPropVerifier arrival(String arrivalDate, String arrivalTime) {
		return segmentDTO -> {
			assertEquals(arrivalDate, segmentDTO.getArrivalDate());
			assertEquals(arrivalTime, segmentDTO.getArrivalTime());
		};
	}

}
