package com.cathaypacific.mmbbizrule.dto.request.rebooking;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class ReBookingAcceptInfoDTO {
	
	private List<String> cancelledSegmentIds;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<String> acceptSegmentIds;
	
	private List<String> atciCancelledSegmentIds;

	public List<String> getCancelledSegmentIds() {
		return cancelledSegmentIds;
	}

	public void setCancelledSegmentIds(List<String> cancelledSegmentIds) {
		this.cancelledSegmentIds = cancelledSegmentIds;
	}

	public List<String> getAcceptSegmentIds() {
		return acceptSegmentIds;
	}

	public void setAcceptSegmentIds(List<String> acceptSegmentIds) {
		this.acceptSegmentIds = acceptSegmentIds;
	}

	public List<String> getAtciCancelledSegmentIds() {
		return atciCancelledSegmentIds;
	}

	public void setAtciCancelledSegmentIds(List<String> atciCancelledSegmentIds) {
		this.atciCancelledSegmentIds = atciCancelledSegmentIds;
	}
	
}
