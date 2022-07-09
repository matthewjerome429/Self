package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product;

import java.util.function.Consumer;

public class ProductValueDTOMocker {

	private ProductValueDTOMocker() {

	}

	public static interface ProductValueDTOPropMocker extends Consumer<ProductValueDTO> {

	}

	public static ProductValueDTO mock(ProductValueDTOPropMocker... propMockers) {
		ProductValueDTO obj = new ProductValueDTO();

		for (ProductValueDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static ProductValueDTOPropMocker value(String value) {
		return productValue -> productValue.setValue(value);
	}
	
	public static ProductValueDTOPropMocker amount(String amount) {
		return productValue -> productValue.setAmount(amount);
	}
	
	public static ProductValueDTOPropMocker currency(String currency) {
		return productValue -> productValue.setCurrency(currency);
	}
	
}
