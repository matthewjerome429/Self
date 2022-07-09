package com.cathaypacific.mmbbizrule.cxservice.dprebook.model.response;


import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class EncryptDeepLinkResponse extends BaseResponseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

    private boolean success;
    
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
    
    
}
