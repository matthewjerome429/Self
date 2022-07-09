package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.List;

public class RetrievePnrRebookInfo {
	
	private Boolean rebooked;
	
	private List<String> newBookedSegmentIds;
	
	private Boolean accepted;
	
	private String acceptFamilyName;
	
	private String acceptGivenName;

	public Boolean isRebooked() {
		return rebooked;
	}

	public void setRebooked(Boolean rebooked) {
		this.rebooked = rebooked;
	}

	public List<String> getNewBookedSegmentIds() {
		return newBookedSegmentIds;
	}

	public void setNewBookedSegmentIds(List<String> newBookedSegmentIds) {
		this.newBookedSegmentIds = newBookedSegmentIds;
	}

	public Boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public String getAcceptFamilyName() {
		return acceptFamilyName;
	}

	public void setAcceptFamilyName(String acceptFamilyName) {
		this.acceptFamilyName = acceptFamilyName;
	}

	public String getAcceptGivenName() {
		return acceptGivenName;
	}

	public void setAcceptGivenName(String acceptGivenName) {
		this.acceptGivenName = acceptGivenName;
	}
	
}
