package com.cathaypacific.mmbbizrule.dto.request.consent;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class PaxConsentDTO {
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String passengerId;
	
	@Valid
	private List<SegmentConsentDTO> segments;
	
	public String getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}
	public List<SegmentConsentDTO> getSegments() {
		return segments;
	}
	public void setSegments(List<SegmentConsentDTO> segments) {
		this.segments = segments;
	}
	
}
