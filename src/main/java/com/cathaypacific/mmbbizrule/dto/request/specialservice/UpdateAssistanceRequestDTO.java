package com.cathaypacific.mmbbizrule.dto.request.specialservice;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.cancelassistance.CancelAssistanceInfoDTO;

public class UpdateAssistanceRequestDTO {

	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<UpdateAssistanceInfoDTO> updateAssistanceInfo;
	
	@Valid
	private List<CancelAssistanceInfoDTO> cancelInfos;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<UpdateAssistanceInfoDTO> getUpdateAssistanceInfo() {
		return updateAssistanceInfo;
	}

	public void setUpdateAssistanceInfo(List<UpdateAssistanceInfoDTO> updateAssistanceInfo) {
		this.updateAssistanceInfo = updateAssistanceInfo;
	}

	public List<CancelAssistanceInfoDTO> getCancelInfos() {
		return cancelInfos;
	}

	public void setCancelInfos(List<CancelAssistanceInfoDTO> cancelInfos) {
		this.cancelInfos = cancelInfos;
	}
	
}
