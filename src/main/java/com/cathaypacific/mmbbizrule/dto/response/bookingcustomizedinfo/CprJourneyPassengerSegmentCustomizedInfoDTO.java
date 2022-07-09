package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CprJourneyPassengerSegmentCustomizedInfoDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 7292220619222782787L;

	private String passengerId;
	
	private String segmentId;
	
	/** Unique flight id, DID */
	private String cprProductIdentifierDID;
	
	private boolean canCheckIn;
	
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

	public String getCprProductIdentifierDID() {
		return cprProductIdentifierDID;
	}

	public void setCprProductIdentifierDID(String cprProductIdentifierDID) {
		this.cprProductIdentifierDID = cprProductIdentifierDID;
	}

	public boolean isCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}
	
}
