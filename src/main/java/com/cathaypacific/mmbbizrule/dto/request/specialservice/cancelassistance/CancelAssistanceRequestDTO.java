package com.cathaypacific.mmbbizrule.dto.request.specialservice.cancelassistance;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class CancelAssistanceRequestDTO {
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<CancelAssistanceInfoDTO> cancelInfos;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<CancelAssistanceInfoDTO> getCancelInfos() {
		return cancelInfos;
	}

	public void setCancelInfos(List<CancelAssistanceInfoDTO> cancelInfos) {
		this.cancelInfos = cancelInfos;
	}
	
}
