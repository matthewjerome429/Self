package com.cathaypacific.mmbbizrule.dto.request.baggage;

import java.util.function.Consumer;

public class ExtraBaggageRequestDTOMocker {

	private ExtraBaggageRequestDTOMocker() {

	}

	public static interface ExtraBaggageRequestDTOPropMocker extends Consumer<ExtraBaggageRequestDTO> {

	}

	public static ExtraBaggageRequestDTO mock(ExtraBaggageRequestDTOPropMocker... propMockers) {
		ExtraBaggageRequestDTO obj = new ExtraBaggageRequestDTO();

		for (ExtraBaggageRequestDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static ExtraBaggageRequestDTOPropMocker rloc(String rloc) {
		return request -> request.setRloc(rloc);
	}
	
}
