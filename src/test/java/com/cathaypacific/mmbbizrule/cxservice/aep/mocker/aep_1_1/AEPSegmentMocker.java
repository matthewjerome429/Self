package com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPSegment;

public class AEPSegmentMocker {

	private AEPSegmentMocker() {

	}

	public static interface AEPSegmentPropMocker extends Consumer<AEPSegment> {

	}

	public static AEPSegment mock(AEPSegmentPropMocker... propMockers) {
		AEPSegment obj = new AEPSegment();

		for (AEPSegmentPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static AEPSegmentPropMocker segmentRef(Integer segmentRef) {
		return aepSegment -> aepSegment.setSegmentRef(segmentRef);
	}

	public static AEPSegmentPropMocker airport(String origin, String destination) {
		return aepSegment -> {
			aepSegment.setOrigin(origin);
			aepSegment.setDestination(destination);
		};
	}

	public static AEPSegmentPropMocker flight(String marketingCarrier, Integer flight) {
		return aepSegment -> {
			aepSegment.setMarketingCarrier(marketingCarrier);
			aepSegment.setFlight(flight);
		};
	}

	public static AEPSegmentPropMocker departureDate(String departureDate) {
		return aepSegment -> aepSegment.setDepartureDate(departureDate);
	}

}
