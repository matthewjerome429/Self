package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.od;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.common.od.BgAlOdSegmentDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od.BgAlOdBookingRequestDTO;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BgAlOdBookingRequestDTOVerifier extends Verifier<BgAlOdBookingRequestDTO> {

	private BgAlOdBookingRequestDTOVerifier() {

	}

	public static interface BgAlOdBookingRequestDTOPropVerifier extends Consumer<BgAlOdBookingRequestDTO> {

	}

	public static BgAlOdBookingRequestDTOVerifier expect(BgAlOdBookingRequestDTOPropVerifier... propVerifiers) {
		BgAlOdBookingRequestDTOVerifier verifier = new BgAlOdBookingRequestDTOVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	public static BgAlOdBookingRequestDTOPropVerifier bookingId(String bookingId) {
		return bookingDTO -> assertEquals(bookingId, bookingDTO.getBookingId());
	}

	@SafeVarargs
	public static BgAlOdBookingRequestDTOPropVerifier segments(Verifier<BgAlOdSegmentDTO>... segmentVerifiers) {
		return bookingDTO -> verifyList(segmentVerifiers, bookingDTO.getSegments());
	}

}
