package com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.model.response;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class PhoneNumberValidationResponseDTO extends BaseResponseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean valid;
	
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	

}
