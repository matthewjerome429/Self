package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CprJourneyPassengerSegmentDTOV2 extends BaseResponseDTO {

	private static final long serialVersionUID = 7292220619222782787L;

	private String passengerId;
	
	private String cprUniqueCustomerId;
	
	private String segmentId;
	
	/** Unique flight id, DID */
	private String cprProductIdentifierDID;

	/** Relate TO Adult's Sector DID, this filed only for infant. */
	private String cprProductIdentifierJID;
	
	private boolean canCheckIn;
	
	private boolean checkedIn;
	
	/** The stand by status returned from CPR */
	private boolean checkInStandBy;
	
	/** Stand by security number */
	private String securityNumber;

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

	public String getCprUniqueCustomerId() {
		return cprUniqueCustomerId;
	}

	public void setCprUniqueCustomerId(String cprUniqueCustomerId) {
		this.cprUniqueCustomerId = cprUniqueCustomerId;
	}

	public String getCprProductIdentifierDID() {
		return cprProductIdentifierDID;
	}

	public void setCprProductIdentifierDID(String cprProductIdentifierDID) {
		this.cprProductIdentifierDID = cprProductIdentifierDID;
	}

	public String getCprProductIdentifierJID() {
		return cprProductIdentifierJID;
	}

	public void setCprProductIdentifierJID(String cprProductIdentifierJID) {
		this.cprProductIdentifierJID = cprProductIdentifierJID;
	}

	public boolean isCheckInStandBy() {
		return checkInStandBy;
	}

	public void setCheckInStandBy(boolean checkInStandBy) {
		this.checkInStandBy = checkInStandBy;
	}

	public String getSecurityNumber() {
		return securityNumber;
	}

	public void setSecurityNumber(String securityNumber) {
		this.securityNumber = securityNumber;
	}
	
}
