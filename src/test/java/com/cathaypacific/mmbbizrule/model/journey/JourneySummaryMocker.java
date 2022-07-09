package com.cathaypacific.mmbbizrule.model.journey;

import java.util.Arrays;
import java.util.function.Consumer;

public class JourneySummaryMocker {

	private JourneySummaryMocker() {

	}

	public static interface JourneySummaryPropMocker extends Consumer<JourneySummary> {

	}

	public static JourneySummary mock(JourneySummaryPropMocker... propMockers) {
		JourneySummary obj = new JourneySummary();

		for (JourneySummaryPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static JourneySummaryPropMocker journeyId(String journeyId) {
		return journeySummary -> journeySummary.setJourneyId(journeyId);
	}
	
	public static JourneySummaryPropMocker segments(JourneySegment... segments) {
		return journeySummary -> journeySummary.setSegments(Arrays.asList(segments));
	}
	
}
