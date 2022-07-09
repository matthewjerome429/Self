package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.util.List;

public class RebookInfoDTO {
	
	private Boolean rebooked;
	
	private List<String> newBookedSegmentIds;
	
	private Boolean accepted;
	
	private String acceptFamilyName;
	
	private String acceptGivenName;

	public Boolean getRebooked() {
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
