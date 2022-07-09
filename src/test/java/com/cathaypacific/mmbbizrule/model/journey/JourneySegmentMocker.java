package com.cathaypacific.mmbbizrule.model.journey;

import java.util.function.Consumer;

public class JourneySegmentMocker {

	private JourneySegmentMocker() {

	}

	public static interface JourneySegmentPropMocker extends Consumer<JourneySegment> {

	}

	public static JourneySegment mock(JourneySegmentPropMocker... propMockers) {
		JourneySegment obj = new JourneySegment();

		for (JourneySegmentPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static JourneySegmentPropMocker segmentId(String segmentId) {
		return journeySegment -> journeySegment.setSegmentId(segmentId);
	}
	
	public static JourneySegmentPropMocker marketSegmentNumber(String marketSegmentNumber) {
		return journeySegment -> journeySegment.setMarketSegmentNumber(marketSegmentNumber);
	}
	
	public static JourneySegmentPropMocker marketCompany(String marketCompany) {
		return journeySegment -> journeySegment.setMarketCompany(marketCompany);
	}
	
}
