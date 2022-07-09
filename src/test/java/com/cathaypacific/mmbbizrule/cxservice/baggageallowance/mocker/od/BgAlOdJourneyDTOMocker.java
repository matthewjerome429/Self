package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.common.od.BgAlOdSegmentDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.BaggageAllowanceDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdJourneyDTO;

public class BgAlOdJourneyDTOMocker {

	private BgAlOdJourneyDTOMocker() {

	}

	public static interface BgAlOdJourneyDTOPropMocker extends Consumer<BgAlOdJourneyDTO> {

	}

	public static BgAlOdJourneyDTO mock(BgAlOdJourneyDTOPropMocker... propMockers) {
		BgAlOdJourneyDTO obj = new BgAlOdJourneyDTO();

		for (BgAlOdJourneyDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static BgAlOdJourneyDTOPropMocker journeyId(String journeyId) {
		return journeyDTO -> journeyDTO.setJourneyId(journeyId);
	}

	public static BgAlOdJourneyDTOPropMocker baggageAllowanceType(String baggageAllowanceType) {
		return journeyDTO -> journeyDTO.setBaggageAllowanceType(baggageAllowanceType);
	}

	public static BgAlOdJourneyDTOPropMocker segments(BgAlOdSegmentDTO... segments) {
		return journeyDTO -> journeyDTO.setSegments(Arrays.asList(segments));
	}

	public static BgAlOdJourneyDTOPropMocker baggageAllowance(BaggageAllowanceDTO... baggageAllowance) {
		return journeyDTO -> journeyDTO.setBaggageAllowance(Arrays.asList(baggageAllowance));
	}

}
