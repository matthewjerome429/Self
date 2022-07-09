package com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1;

import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductValue;

public class AEPProductValueMocker {

	private AEPProductValueMocker() {

	}

	public static interface AEPProductValuePropMocker extends Consumer<AEPProductValue> {

	}

	public static AEPProductValue mock(AEPProductValuePropMocker... propMockers) {
		AEPProductValue obj = new AEPProductValue();

		for (AEPProductValuePropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}

	public static AEPProductValuePropMocker value(String value) {
		return productValue -> productValue.setValue(value);
	}

	public static AEPProductValuePropMocker amount(String amount) {
		return productValue -> productValue.setAmount(amount);
	}

	public static AEPProductValuePropMocker currency(String currency) {
		return productValue -> productValue.setCurrency(currency);
	}

}
