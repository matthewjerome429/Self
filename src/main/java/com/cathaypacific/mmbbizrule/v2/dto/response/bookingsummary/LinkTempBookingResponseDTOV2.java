package com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

import io.swagger.annotations.ApiModelProperty;

public class LinkTempBookingResponseDTOV2 extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5549700913724846996L;
	/**
	 * linked flag
	 */
	@ApiModelProperty(value = "Success flag", required = true, example = "true")
	private Boolean success = false;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	

}
