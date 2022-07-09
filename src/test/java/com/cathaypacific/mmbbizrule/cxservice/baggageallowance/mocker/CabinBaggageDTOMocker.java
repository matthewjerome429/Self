package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinBaggageDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinBaggagePieceDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinBaggageWeightDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinDimensionsDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CabinSmallItemDTO;

public class CabinBaggageDTOMocker {

	private CabinBaggageDTOMocker() {

	}

	public static interface CabinBaggageDTOPropMocker extends Consumer<CabinBaggageDTO> {

	}

	public static CabinBaggageDTO mock(CabinBaggageDTOPropMocker... propMockers) {
		CabinBaggageDTO obj = new CabinBaggageDTO();

		for (CabinBaggageDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CabinBaggageDTOPropMocker weight(CabinBaggageWeightDTO weight) {
		return cabinBaggageDTO -> cabinBaggageDTO.setWeight(weight);
	}

	public static CabinBaggageDTOPropMocker piece(CabinBaggagePieceDTO piece) {
		return cabinBaggageDTO -> cabinBaggageDTO.setPiece(piece);
	}

	public static CabinBaggageDTOPropMocker smallItem(CabinSmallItemDTO smallItem) {
		return cabinBaggageDTO -> cabinBaggageDTO.setSmallItem(smallItem);
	}

	public static CabinBaggageDTOPropMocker dimensions(CabinDimensionsDTO dimensions) {
		return cabinBaggageDTO -> cabinBaggageDTO.setDimensions(dimensions);
	}

}
