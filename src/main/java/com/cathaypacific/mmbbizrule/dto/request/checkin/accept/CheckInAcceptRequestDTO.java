package com.cathaypacific.mmbbizrule.dto.request.checkin.accept;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class CheckInAcceptRequestDTO {
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String journeyId;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<String> passengerIds;

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

	public List<String> getPassengerIds() {
		return passengerIds;
	}

	public void setPassengerIds(List<String> passengerIds) {
		this.passengerIds = passengerIds;
	}
	
}
