package com.cathaypacific.mmbbizrule.v2.dto.request.updateseat;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class UpdateSeatRequestDTOV2 {
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String segmentId;
	
	private String journeyId;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Valid
	private List<PaxSeatDetailDTOV2> paxSeatDetails;
	
	public String getRloc() {
		return rloc;
	}
	public void setRloc(String rloc) {
		this.rloc = rloc;
	}
	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	public String getJourneyId() {
		return journeyId;
	}
	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}
	public List<PaxSeatDetailDTOV2> getPaxSeatDetails() {
		return paxSeatDetails;
	}
	public void setPaxSeatDetails(List<PaxSeatDetailDTOV2> paxSeatDetails) {
		this.paxSeatDetails = paxSeatDetails;
	}
}
