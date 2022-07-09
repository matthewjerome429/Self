package com.cathaypacific.mmbbizrule.dto.response.specialservice.cancelassistance;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CancelAssistanceResponseDTO extends BaseResponseDTO {
	
	private static final long serialVersionUID = 3844547712016967320L;
	
	private boolean success;

	public CancelAssistanceResponseDTO() {
		super();
	}

	public CancelAssistanceResponseDTO(boolean success) {
		super();
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
