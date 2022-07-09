package com.cathaypacific.mmbbizrule.model.detail.verifier;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class SegmentVerifier extends Verifier<Segment> {

	private SegmentVerifier() {

	}

	public static interface SegmentPropVerifier extends Consumer<Segment> {

	}

	public static SegmentVerifier expect(SegmentPropVerifier... propVerifiers) {
		SegmentVerifier verifier = new SegmentVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	public static SegmentPropVerifier segmentID(String segmentID) {
		return segment -> assertEquals(segmentID, segment.getSegmentID());
	}

}
