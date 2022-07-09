package com.cathaypacific.mmbbizrule.dto.request.updateseat;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class UpdateSeatRequestDTO {
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String segmentId;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<PaxSeatDetail> paxSeatDetails;
	
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
	public List<PaxSeatDetail> getPaxSeatDetails() {
		return paxSeatDetails;
	}
	public void setPaxSeatDetails(List<PaxSeatDetail> paxSeatDetails) {
		this.paxSeatDetails = paxSeatDetails;
	}
}
