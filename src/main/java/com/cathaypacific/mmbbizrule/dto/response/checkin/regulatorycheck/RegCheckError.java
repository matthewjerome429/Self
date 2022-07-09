package com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

import io.swagger.annotations.ApiModelProperty;

public class RegCheckError extends ErrorInfo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * to identify if the error is reg inter active error
	 */
	@ApiModelProperty("The flag of reg inter active error(user can fix by update traveldoc)")
	private boolean interActiveError;
	
	@ApiModelProperty("The flag of this error is inter active error and it related to visa")
	private boolean visaRelated;

	public boolean isInterActiveError() {
		return interActiveError;
	}

	public void setInterActiveError(boolean interActiveError) {
		this.interActiveError = interActiveError;
	}

	public boolean isVisaRelated() {
		return visaRelated;
	}

	public void setVisaRelated(boolean visaRelated) {
		this.visaRelated = visaRelated;
	}

}
