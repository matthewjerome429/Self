package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinSmallItemDTO;

public class CabinSmallItemDTOMocker {

	private CabinSmallItemDTOMocker() {

	}

	public static interface CabinSmallItemDTOPropMocker extends Consumer<CabinSmallItemDTO> {

	}

	public static CabinSmallItemDTO mock(CabinSmallItemDTOPropMocker... propMockers) {
		CabinSmallItemDTO obj = new CabinSmallItemDTO();

		for (CabinSmallItemDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CabinSmallItemDTOPropMocker standard(int standard) {
		return cabinSmallItemDTO -> cabinSmallItemDTO.setStandard(standard);
	}

	public static CabinSmallItemDTOPropMocker member(int member) {
		return cabinSmallItemDTO -> cabinSmallItemDTO.setMember(member);
	}

}
