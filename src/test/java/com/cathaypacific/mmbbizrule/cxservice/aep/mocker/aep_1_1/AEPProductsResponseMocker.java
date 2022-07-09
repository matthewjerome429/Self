package com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;

public class AEPProductsResponseMocker {

	private AEPProductsResponseMocker() {

	}

	public static interface AEPProductsResponsePropMocker extends Consumer<AEPProductsResponse> {

	}

	public static AEPProductsResponse mock(AEPProductsResponsePropMocker... propMockers) {
		AEPProductsResponse obj = new AEPProductsResponse();

		for (AEPProductsResponsePropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static AEPProductsResponsePropMocker products(AEPProduct... aepProducts) {
		return aepProductResponse -> aepProductResponse.setProducts(Arrays.asList(aepProducts));
	}

}
