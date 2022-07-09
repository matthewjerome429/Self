package com.cathaypacific.mmbbizrule.dto.request.retrievepnr;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.dto.common.RequiredInfoDTO;

public class RetrievePnrByRlocMemberRequestDTO extends RequiredInfoDTO{
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "^[A-Za-z0-9]{6,7}$",message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String rloc;
	
	public String getRloc() {
		return rloc;
	}
	public void setRloc(String rloc) {
		this.rloc = rloc;
	}
	
}
