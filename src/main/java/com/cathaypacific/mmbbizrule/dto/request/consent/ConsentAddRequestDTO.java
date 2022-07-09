package com.cathaypacific.mmbbizrule.dto.request.consent;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class ConsentAddRequestDTO {

	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;

	@Valid
	private List<PaxConsentDTO> passengers;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<PaxConsentDTO> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<PaxConsentDTO> passengers) {
		this.passengers = passengers;
	}
}
