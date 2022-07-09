package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product;

import java.util.Arrays;
import java.util.function.Consumer;

import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;

public class ProductsResponseDTOMocker {

	private ProductsResponseDTOMocker() {

	}

	public static interface ProductsResponseDTOPropMocker extends Consumer<ProductsResponseDTO> {

	}

	public static ProductsResponseDTO mock(ProductsResponseDTOPropMocker... propMockers) {
		ProductsResponseDTO obj = new ProductsResponseDTO();

		for (ProductsResponseDTOPropMocker propSetter : propMockers) {
			propSetter.accept(obj);
		}

		return obj;
	}
	
	public static ProductsResponseDTOPropMocker products(ProductDTO... products) {
		return response -> response.setProducts(Arrays.asList(products));
	}
	
}
