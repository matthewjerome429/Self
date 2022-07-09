package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.util.List;

public class RebookMappingDTOV2 {
	
	private List<String> cancelledSegmentIds;
	
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
