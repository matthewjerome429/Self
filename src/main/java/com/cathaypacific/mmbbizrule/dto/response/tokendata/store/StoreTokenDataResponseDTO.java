package com.cathaypacific.mmbbizrule.dto.response.tokendata.store;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

import io.swagger.annotations.ApiModelProperty;

public class StoreTokenDataResponseDTO extends BaseResponseDTO{

	private static final long serialVersionUID = -2913754203364842261L;
	
	/** The key used to get the token value again */
	@ApiModelProperty("The key used to get the token value again")
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
