package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.BaggageAllowanceDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinBaggageDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CheckinBaggageDTO;

public class BaggageAllowanceDTOMocker {

	private BaggageAllowanceDTOMocker() {

	}

	public static interface BaggageAllowanceDTOPropMocker extends Consumer<BaggageAllowanceDTO> {

	}

	public static BaggageAllowanceDTO mock(BaggageAllowanceDTOPropMocker... propMockers) {
		BaggageAllowanceDTO obj = new BaggageAllowanceDTO();

		for (BaggageAllowanceDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static BaggageAllowanceDTOPropMocker memberTier(String memberTier) {
		return baggageAllowanceDTO -> baggageAllowanceDTO.setMemberTier(memberTier);
	}

	public static BaggageAllowanceDTOPropMocker cabinBaggage(CabinBaggageDTO cabinBaggage) {
		return baggageAllowanceDTO -> baggageAllowanceDTO.setCabinBaggage(cabinBaggage);
	}

	public static BaggageAllowanceDTOPropMocker checkinBaggage(CheckinBaggageDTO checkinBaggage) {
		return baggageAllowanceDTO -> baggageAllowanceDTO.setCheckinBaggage(checkinBaggage);
	}

}
