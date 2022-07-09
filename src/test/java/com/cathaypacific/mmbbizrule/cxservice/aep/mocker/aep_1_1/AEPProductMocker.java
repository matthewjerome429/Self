package com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPAirProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPPriceItem;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductValue;

public class AEPProductMocker {

	private AEPProductMocker() {

	}

	public static interface AEPProductPropMocker extends Consumer<AEPProduct> {

	}

	public static AEPProduct mock(AEPProductPropMocker... propMockers) {
		AEPProduct obj = new AEPProduct();

		for (AEPProductPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static AEPProductPropMocker productId(String productId) {
		return aepProduct -> aepProduct.setProductId(productId);
	}

	public static AEPProductPropMocker sellOnOffline(boolean sellOnOffline) {
		return aepProduct -> aepProduct.setSellOnOffline(sellOnOffline);
	}

	public static AEPProductPropMocker airProduct(AEPAirProduct... aepAirProducts) {
		return aepProduct -> aepProduct.setAirProduct(Arrays.asList(aepAirProducts));
	}

	public static AEPProductPropMocker totalValue(AEPPriceItem aepPriceItem) {
		return aepProduct -> aepProduct.setTotalValue(aepPriceItem);
	}

	public static AEPProductPropMocker productValues(AEPProductValue... aepProductValues) {
		return aepProduct -> aepProduct.setProductValues(Arrays.asList(aepProductValues));
	}

}
