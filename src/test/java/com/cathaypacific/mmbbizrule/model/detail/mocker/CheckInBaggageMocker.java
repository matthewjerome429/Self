package com.cathaypacific.mmbbizrule.model.detail.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.model.booking.detail.BaggageDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.CheckInBaggage;

public class CheckInBaggageMocker {

	private CheckInBaggageMocker() {

	}

	public static interface CheckInBaggagePropMocker extends Consumer<CheckInBaggage> {

	}

	public static CheckInBaggage mock(CheckInBaggagePropMocker... propMockers) {
		CheckInBaggage obj = new CheckInBaggage();

		for (CheckInBaggagePropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CheckInBaggagePropMocker standardBaggage(BaggageDetail standardBaggage) {
		return checkInBaggage -> checkInBaggage.setStandardBaggage(standardBaggage);
	}

	public static CheckInBaggagePropMocker waiverBaggage(BaggageDetail waiverBaggage) {
		return checkInBaggage -> checkInBaggage.setWaiverBaggage(waiverBaggage);
	}

	public static CheckInBaggagePropMocker memberBaggage(BaggageDetail memberBaggage) {
		return checkInBaggage -> checkInBaggage.setMemberBaggage(memberBaggage);
	}

	public static CheckInBaggagePropMocker limit(BaggageDetail limit) {
		return checkInBaggage -> checkInBaggage.setLimit(limit);
	}

}
