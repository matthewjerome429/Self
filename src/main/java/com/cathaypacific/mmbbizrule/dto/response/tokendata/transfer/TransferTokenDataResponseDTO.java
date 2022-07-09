package com.cathaypacific.mmbbizrule.dto.response.tokendata.transfer;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class TransferTokenDataResponseDTO extends BaseResponseDTO{

	private static final long serialVersionUID = -4936762167378685335L;
	
	private Boolean success;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}
