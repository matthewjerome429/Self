package com.cathaypacific.mmbbizrule.dto.response.baggage;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import io.swagger.annotations.ApiModelProperty;

public class ExtraBaggageResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 8378073163398212678L;

	@ApiModelProperty(value = "Baggage products", required = true)
	private List<BaggageProductDTO> products;

	public List<BaggageProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<BaggageProductDTO> products) {
		this.products = products;
	}

}
