package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinBaggageWeightDTO;

public class CabinBaggageWeightDTOMocker {

	private CabinBaggageWeightDTOMocker() {

	}

	public static interface CabinBaggageWeightDTOPropMocker extends Consumer<CabinBaggageWeightDTO> {

	}

	public static CabinBaggageWeightDTO mock(CabinBaggageWeightDTOPropMocker... propMockers) {
		CabinBaggageWeightDTO obj = new CabinBaggageWeightDTO();

		for (CabinBaggageWeightDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CabinBaggageWeightDTOPropMocker standard(int standard) {
		return cabinBaggageWeightDTO -> cabinBaggageWeightDTO.setStandard(standard);
	}

	public static CabinBaggageWeightDTOPropMocker member(int member) {
		return cabinBaggageWeightDTO -> cabinBaggageWeightDTO.setMember(member);
	}

}
