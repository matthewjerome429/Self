package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

import io.swagger.annotations.ApiModelProperty;

public class ProductsResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = -6445345709953434710L;

	@ApiModelProperty(value = "Purchasable products", required = true)
	private List<ProductDTO> products;

	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}

}
