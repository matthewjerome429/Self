package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.List;

public class RetrievePnrRebookMapping {
	
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
