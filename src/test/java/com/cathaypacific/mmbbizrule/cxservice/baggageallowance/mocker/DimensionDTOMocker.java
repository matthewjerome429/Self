package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.DimensionDTO;

public class DimensionDTOMocker {

	private DimensionDTOMocker() {

	}

	public static interface DimensionDTOPropMocker extends Consumer<DimensionDTO> {

	}

	public static DimensionDTO mock(DimensionDTOPropMocker... propMockers) {
		DimensionDTO obj = new DimensionDTO();

		for (DimensionDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static DimensionDTOPropMocker values(int length, int width, int height) {
		return dimensionDTO -> {
			dimensionDTO.setLength(length);
			dimensionDTO.setWidth(width);
			dimensionDTO.setHeight(height);
		};
	}

}
