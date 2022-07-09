package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdBookingResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdResponseDTO;

public class BgAlOdResponseDTOMocker {

	private BgAlOdResponseDTOMocker() {

	}

	public static interface BgAlOdResponseDTOPropMocker extends Consumer<BgAlOdResponseDTO> {

	}

	public static BgAlOdResponseDTO mock(BgAlOdResponseDTOPropMocker... propMockers) {
		BgAlOdResponseDTO obj = new BgAlOdResponseDTO();

		for (BgAlOdResponseDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static BgAlOdResponseDTOPropMocker bookings(BgAlOdBookingResponseDTO... bookings) {
		return responseDTO -> responseDTO.setBookings(Arrays.asList(bookings));
	}

}
