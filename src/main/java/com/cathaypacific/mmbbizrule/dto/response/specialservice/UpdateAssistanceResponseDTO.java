package com.cathaypacific.mmbbizrule.dto.response.specialservice;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class UpdateAssistanceResponseDTO extends BaseResponseDTO {
	
	private static final long serialVersionUID = 3358763067102542211L;
	
	private Boolean success;
	
	public UpdateAssistanceResponseDTO() {
		super();
	}
	
	public UpdateAssistanceResponseDTO(boolean success) {
		super();
		this.success = success;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
}
