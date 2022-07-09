package com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.olciconsumer.model.request.BaseRequestDTO;

public class SPBPRequestDTOV2 extends BaseRequestDTO {
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String journeyId;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<String> uniqueCustomerIds;
	
	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<String> getUniqueCustomerIds() {
		return uniqueCustomerIds;
	}

	public void setUniqueCustomerIds(List<String> uniqueCustomerIds) {
		this.uniqueCustomerIds = uniqueCustomerIds;
	}
	
}
