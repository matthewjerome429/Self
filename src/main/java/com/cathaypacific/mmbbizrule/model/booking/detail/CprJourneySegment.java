package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.util.List;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class CprJourneySegment {
	
	private String segmentId;
	
	/**
	 * these two fields are from first FlightDTO from CPR
	 * */
	private boolean displayOnly;
	private boolean canCheckIn;
	
	/**
	 * these errors are combined & distinct from CPR
	 * */
	private List<ErrorInfo> errors;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public boolean isDisplayOnly() {
		return displayOnly;
	}

	public void setDisplayOnly(boolean displayOnly) {
		this.displayOnly = displayOnly;
	}

	public boolean getCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}

	public List<ErrorInfo> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorInfo> errors) {
		this.errors = errors;
	}
	
}
