package com.cathaypacific.mmbbizrule.dto.response.rebooking;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class RebookAcceptResponseDTO extends BaseResponseDTO {
	
	private static final long serialVersionUID = 6243837366814819313L;
	
	private Boolean success;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
}
