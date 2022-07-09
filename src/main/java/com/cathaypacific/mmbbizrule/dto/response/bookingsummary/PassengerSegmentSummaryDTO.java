package com.cathaypacific.mmbbizrule.dto.response.bookingsummary;

import java.io.Serializable;

public class PassengerSegmentSummaryDTO implements Serializable{
	
	private static final long serialVersionUID = -2924530101803090001L;
	// passenger id
	private String passengerId;
	// segment id
	private String segmentId;
	// the passenger has checked in on this flight
	private boolean checkedIn;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}
	
}
