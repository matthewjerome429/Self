package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CheckinBaggageWeightDTO;

public class CheckinBaggageWeightDTOMocker {

	private CheckinBaggageWeightDTOMocker() {

	}

	public static interface CheckinBaggageWeightDTOPropMocker extends Consumer<CheckinBaggageWeightDTO> {

	}

	public static CheckinBaggageWeightDTO mock(CheckinBaggageWeightDTOPropMocker... propMockers) {
		CheckinBaggageWeightDTO obj = new CheckinBaggageWeightDTO();

		for (CheckinBaggageWeightDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CheckinBaggageWeightDTOPropMocker standard(int standard) {
		return checkinBaggageWeightDTO -> checkinBaggageWeightDTO.setStandard(standard);
	}

	public static CheckinBaggageWeightDTOPropMocker member(int member) {
		return checkinBaggageWeightDTO -> checkinBaggageWeightDTO.setMember(member);
	}

	public static CheckinBaggageWeightDTOPropMocker infant(int infant) {
		return checkinBaggageWeightDTO -> checkinBaggageWeightDTO.setInfant(infant);
	}

}
