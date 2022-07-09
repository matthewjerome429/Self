package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.PriceDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.ProductTypeEnum;

public class ProductDTOMocker {

	private ProductDTOMocker() {

	}

	public static interface ProductDTOPropMocker extends Consumer<ProductDTO> {

	}

	public static ProductDTO mock(ProductDTOPropMocker... propMockers) {
		ProductDTO obj = new ProductDTO();

		for (ProductDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static ProductDTOPropMocker productType(ProductTypeEnum productType) {
		return product -> product.setProductType(productType);
	}
	
	public static ProductDTOPropMocker segmentIds(String... segmentIds) {
		return product -> product.setSegmentIds(Arrays.asList(segmentIds));
	}
	
	public static ProductDTOPropMocker passengerId(String passengerId) {
		return product -> product.setPassengerId(passengerId);
	}
	
	public static ProductDTOPropMocker unit(String unit) {
		return product -> product.setUnit(unit);
	}
	
	public static ProductDTOPropMocker sellOnOffline(boolean sellOnOffline) {
		return product -> product.setSellOnOffline(sellOnOffline);
	}
	
	public static ProductDTOPropMocker productValues(ProductValueDTO... productValues) {
		return product -> product.setProductValues(Arrays.asList(productValues));
	}
	
	public static ProductDTOPropMocker price(PriceDTO price) {
		return product -> product.setPrice(price);
	}
	
}
