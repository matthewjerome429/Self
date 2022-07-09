package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.CheckinBaggagePieceDTO;

public class CheckinBaggagePieceDTOMocker {

	private CheckinBaggagePieceDTOMocker() {

	}

	public static interface CheckinBaggagePieceDTOPropMocker extends Consumer<CheckinBaggagePieceDTO> {

	}

	public static CheckinBaggagePieceDTO mock(CheckinBaggagePieceDTOPropMocker... propMockers) {
		CheckinBaggagePieceDTO obj = new CheckinBaggagePieceDTO();

		for (CheckinBaggagePieceDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static CheckinBaggagePieceDTOPropMocker standard(int standard) {
		return checkinBaggagePieceDTO -> checkinBaggagePieceDTO.setStandard(standard);
	}

	public static CheckinBaggagePieceDTOPropMocker member(int member) {
		return checkinBaggagePieceDTO -> checkinBaggagePieceDTO.setMember(member);
	}

	public static CheckinBaggagePieceDTOPropMocker infant(int infant) {
		return checkinBaggagePieceDTO -> checkinBaggagePieceDTO.setInfant(infant);
	}

}
