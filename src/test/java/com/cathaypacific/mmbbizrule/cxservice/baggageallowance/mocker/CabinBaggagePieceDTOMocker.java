package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinBaggagePieceDTO;

public class CabinBaggagePieceDTOMocker {

	private CabinBaggagePieceDTOMocker() {

	}

	public static interface CabinBaggagePieceDTOPropMocker extends Consumer<CabinBaggagePieceDTO> {

	}

	public static CabinBaggagePieceDTO mock(CabinBaggagePieceDTOPropMocker... propMockers) {
		CabinBaggagePieceDTO obj = new CabinBaggagePieceDTO();

		for (CabinBaggagePieceDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CabinBaggagePieceDTOPropMocker standard(int standard) {
		return cabinBaggagePieceDTO -> cabinBaggagePieceDTO.setStandard(standard);
	}

	public static CabinBaggagePieceDTOPropMocker member(int member) {
		return cabinBaggagePieceDTO -> cabinBaggagePieceDTO.setMember(member);
	}

}
