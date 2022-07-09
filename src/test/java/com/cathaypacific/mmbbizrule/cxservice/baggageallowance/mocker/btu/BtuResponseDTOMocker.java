package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.btu;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.BaggageAllowanceDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BtuResponseDTO;

public class BtuResponseDTOMocker {

	private BtuResponseDTOMocker() {

	}

	public static interface BtuResponseDTOPropMocker extends Consumer<BtuResponseDTO> {

	}

	public static BtuResponseDTO mock(BtuResponseDTOPropMocker... propMockers) {
		BtuResponseDTO obj = new BtuResponseDTO();

		for (BtuResponseDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static BtuResponseDTOPropMocker btuId(String btuId) {
		return btuDTO -> btuDTO.setBtuId(btuId);
	}

	public static BtuResponseDTOPropMocker baggageAllowance(BaggageAllowanceDTO... baggageAllowance) {
		return btuDTO -> btuDTO.setBaggageAllowance(Arrays.asList(baggageAllowance));
	}

}
