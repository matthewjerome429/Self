package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.util.List;

public class RebookMappingDTO {
	
	private List<String> cancelledSegmentIds;
	
	private List<String> acceptSegmentIds;

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
	
}
