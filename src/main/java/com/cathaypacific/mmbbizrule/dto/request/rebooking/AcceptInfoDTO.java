package com.cathaypacific.mmbbizrule.dto.request.rebooking;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class AcceptInfoDTO {
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<AcceptFlightInfoDTO> cancelledFlightInfos;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<AcceptFlightInfoDTO> confirmedFlightInfos;

	public List<AcceptFlightInfoDTO> getCancelledFlightInfos() {
		return cancelledFlightInfos;
	}

	public void setCancelledFlightInfos(List<AcceptFlightInfoDTO> cancelledFlightInfos) {
		this.cancelledFlightInfos = cancelledFlightInfos;
	}

	public List<AcceptFlightInfoDTO> getConfirmedFlightInfos() {
		return confirmedFlightInfos;
	}

	public void setConfirmedFlightInfos(List<AcceptFlightInfoDTO> confirmedFlightInfos) {
		this.confirmedFlightInfos = confirmedFlightInfos;
	}

}
