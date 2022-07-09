package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.math.BigInteger;
import java.util.function.Consumer;

import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;
import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageDetail;

public class BaggageDetailMocker {

	private BaggageDetailMocker() {

	}

	public static interface BaggageDetailPropMocker extends Consumer<BaggageDetail> {

	}

	public static BaggageDetail mock(BaggageDetailPropMocker... propMockers) {
		BaggageDetail obj = new BaggageDetail();

		for (BaggageDetailPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static BaggageDetailPropMocker value(BigInteger amount, BaggageUnitEnum unit) {
		return baggageDetail -> {
			baggageDetail.setAmount(amount);
			baggageDetail.setUnit(unit);
		};
	}

}
