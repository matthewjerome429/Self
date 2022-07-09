package com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPAirProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPPassengerWithName;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPSegment;

public class AEPAirProductMocker {

	private AEPAirProductMocker() {

	}

	public static interface AEPAirProductPropMocker extends Consumer<AEPAirProduct> {

	}

	public static AEPAirProduct mock(AEPAirProductPropMocker... propMockers) {
		AEPAirProduct obj = new AEPAirProduct();

		for (AEPAirProductPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static AEPAirProductPropMocker segments(AEPSegment... aepSegments) {
		return aepAirProduct -> aepAirProduct.setSegments(Arrays.asList(aepSegments));
	}
	
	public static AEPAirProductPropMocker passengers(AEPPassengerWithName... aepPassengers) {
		return aepAirProduct -> aepAirProduct.setPassengers(Arrays.asList(aepPassengers));
	}
	
}
