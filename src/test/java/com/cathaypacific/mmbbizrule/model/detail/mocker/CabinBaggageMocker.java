package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.CabinBaggage;

public class CabinBaggageMocker {

	private CabinBaggageMocker() {

	}

	public static interface CabinBaggagePropMocker extends Consumer<CabinBaggage> {

	}

	public static CabinBaggage mock(CabinBaggagePropMocker... propMockers) {
		CabinBaggage obj = new CabinBaggage();

		for (CabinBaggagePropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CabinBaggagePropMocker standardBaggage(BaggageDetail standardBaggage) {
		return cabinBaggage -> cabinBaggage.setStandardBaggage(standardBaggage);
	}

	public static CabinBaggagePropMocker memberBaggage(BaggageDetail memberBaggage) {
		return cabinBaggage -> cabinBaggage.setMemberBaggage(memberBaggage);
	}

	public static CabinBaggagePropMocker total(BaggageDetail total) {
		return cabinBaggage -> cabinBaggage.setTotal(total);
	}

	public static CabinBaggagePropMocker limit(BaggageDetail limit) {
		return cabinBaggage -> cabinBaggage.setLimit(limit);
	}

}
