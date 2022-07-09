package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.od;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od.BgAlOdBookingRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od.BgAlOdRequestDTO;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

public class BgAlOdRequestDTOVerifier extends Verifier<BgAlOdRequestDTO> {

	private BgAlOdRequestDTOVerifier() {

	}

	public static interface BgAlOdRequestDTOPropVerifier extends Consumer<BgAlOdRequestDTO> {

	}

	public static BgAlOdRequestDTOVerifier expect(BgAlOdRequestDTOPropVerifier... propVerifiers) {
		BgAlOdRequestDTOVerifier verifier = new BgAlOdRequestDTOVerifier();
		verifier.propVerifiers = propVerifiers;
		return verifier;
	}

	@SafeVarargs
	public static BgAlOdRequestDTOPropVerifier bookings(Verifier<BgAlOdBookingRequestDTO>... bookingVerifiers) {
		return requestDTO -> verifyList(bookingVerifiers, requestDTO.getBookings());
	}

}
