package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinDimensionsDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.DimensionDTO;

public class CabinDimensionsDTOMocker {

	private CabinDimensionsDTOMocker() {

	}

	public static interface CabinDimensionsDTOPropMocker extends Consumer<CabinDimensionsDTO> {

	}

	public static CabinDimensionsDTO mock(CabinDimensionsDTOPropMocker... propMockers) {
		CabinDimensionsDTO obj = new CabinDimensionsDTO();

		for (CabinDimensionsDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CabinDimensionsDTOPropMocker standard(DimensionDTO standard) {
		return cabinDimensionsDTO -> cabinDimensionsDTO.setStandard(standard);
	}

	public static CabinDimensionsDTOPropMocker small(DimensionDTO small) {
		return cabinDimensionsDTO -> cabinDimensionsDTO.setSmall(small);
	}

}
