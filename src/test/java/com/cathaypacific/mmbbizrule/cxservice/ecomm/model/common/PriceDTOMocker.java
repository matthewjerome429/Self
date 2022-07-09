package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common;

import java.util.function.Consumer;

public class PriceDTOMocker {

	private PriceDTOMocker() {

	}

	public static interface PriceDTOPropMocker extends Consumer<PriceDTO> {

	}

	public static PriceDTO mock(PriceDTOPropMocker... propMockers) {
		PriceDTO obj = new PriceDTO();

		for (PriceDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static PriceDTOPropMocker amount(String amount) {
		return price -> price.setAmount(amount);
	}
	
	public static PriceDTOPropMocker currency(String currency) {
		return price -> price.setCurrency(currency);
	}
	
}
