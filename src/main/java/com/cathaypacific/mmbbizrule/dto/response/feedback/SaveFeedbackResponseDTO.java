package com.cathaypacific.mmbbizrule.dto.response.feedback;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class SaveFeedbackResponseDTO extends BaseResponseDTO {
	
	private static final long serialVersionUID = -1625964815497317109L;
	
	private Boolean success;

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}
