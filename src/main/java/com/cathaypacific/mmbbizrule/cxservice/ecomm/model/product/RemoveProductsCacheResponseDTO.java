package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

import io.swagger.annotations.ApiModelProperty;

public class RemoveProductsCacheResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 4696062846401430692L;
	
	@ApiModelProperty(value = "true if products response is found in cache and removed, or false if it's not found", required = true)
	private boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
