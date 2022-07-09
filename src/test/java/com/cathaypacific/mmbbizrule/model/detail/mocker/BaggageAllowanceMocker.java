package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageAllowance;
import com.cathaypacific.mmbbizrule.model.booking.detail.CabinBaggage;
import com.cathaypacific.mmbbizrule.model.booking.detail.CheckInBaggage;

public class BaggageAllowanceMocker {

	private BaggageAllowanceMocker() {

	}

	public static interface BaggageAllowancePropMocker extends Consumer<BaggageAllowance> {

	}

	public static BaggageAllowance mock(BaggageAllowancePropMocker... propMockers) {
		BaggageAllowance obj = new BaggageAllowance();

		for (BaggageAllowancePropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static BaggageAllowancePropMocker checkInBaggage(CheckInBaggage checkInBaggage) {
		return baggageAllowance -> baggageAllowance.setCheckInBaggage(checkInBaggage);
	}

	public static BaggageAllowancePropMocker cabinBaggage(CabinBaggage cabinBaggage) {
		return baggageAllowance -> baggageAllowance.setCabinBaggage(cabinBaggage);
	}

}
