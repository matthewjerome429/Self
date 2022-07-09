package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdBookingResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdJourneyDTO;

public class BgAlOdBookingResponseDTOMocker {

	private BgAlOdBookingResponseDTOMocker() {

	}

	public static interface BgAlOdBookingResponseDTOPropMocker extends Consumer<BgAlOdBookingResponseDTO> {

	}

	public static BgAlOdBookingResponseDTO mock(BgAlOdBookingResponseDTOPropMocker... propMockers) {
		BgAlOdBookingResponseDTO obj = new BgAlOdBookingResponseDTO();

		for (BgAlOdBookingResponseDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static BgAlOdBookingResponseDTOPropMocker bookingId(String bookingId) {
		return bookingResponseDTO -> bookingResponseDTO.setBookingId(bookingId);
	}

	public static BgAlOdBookingResponseDTOPropMocker journeys(BgAlOdJourneyDTO... journeys) {
		return bookingResponseDTO -> bookingResponseDTO.setJourneys(Arrays.asList(journeys));
	}

}
