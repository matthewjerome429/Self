package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CheckinBaggageDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CheckinBaggagePieceDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CheckinBaggageWeightDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.DimensionDTO;

public class CheckinBaggageDTOMocker {

	private CheckinBaggageDTOMocker() {

	}

	public static interface CheckinBaggageDTOPropMocker extends Consumer<CheckinBaggageDTO> {

	}

	public static CheckinBaggageDTO mock(CheckinBaggageDTOPropMocker... propMockers) {
		CheckinBaggageDTO obj = new CheckinBaggageDTO();

		for (CheckinBaggageDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CheckinBaggageDTOPropMocker weight(CheckinBaggageWeightDTO weight) {
		return checkinBaggageDTO -> checkinBaggageDTO.setWeight(weight);
	}

	public static CheckinBaggageDTOPropMocker piece(CheckinBaggagePieceDTO piece) {
		return checkinBaggageDTO -> checkinBaggageDTO.setPiece(piece);
	}

	public static CheckinBaggageDTOPropMocker dimension(DimensionDTO dimension) {
		return checkinBaggageDTO -> checkinBaggageDTO.setDimension(dimension);
	}

}
