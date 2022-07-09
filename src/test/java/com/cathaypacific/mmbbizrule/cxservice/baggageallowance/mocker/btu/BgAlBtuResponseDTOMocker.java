package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.btu;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BgAlBtuResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BtuResponseDTO;

public class BgAlBtuResponseDTOMocker {

	private BgAlBtuResponseDTOMocker() {

	}

	public static interface BgAlBtuResponseDTOPropMocker extends Consumer<BgAlBtuResponseDTO> {

	}

	public static BgAlBtuResponseDTO mock(BgAlBtuResponseDTOPropMocker... propMockers) {
		BgAlBtuResponseDTO obj = new BgAlBtuResponseDTO();

		for (BgAlBtuResponseDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static BgAlBtuResponseDTOPropMocker btu(BtuResponseDTO... btu) {
		return responseDTO -> responseDTO.setBtu(Arrays.asList(btu));
	}

}
