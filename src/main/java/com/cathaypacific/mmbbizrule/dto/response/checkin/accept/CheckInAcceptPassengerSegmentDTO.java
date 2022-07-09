package com.cathaypacific.mmbbizrule.dto.response.checkin.accept;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CheckInAcceptPassengerSegmentDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 8532040101571372421L;
	
	private String passengerId;
	
	private String cprUniqueCustomerId;
	
	private String segmentId;
	
	/** Unique flight id, DID */
	private String cprProductIdentifierDID;

	/** Relate TO Adult's Sector DID, this filed only for infant. */
	private String cprProductIdentifierJID;
	
	private boolean checkedIn;
	
	private String seatNumber;
	
	private String originalSeatNumber;
	
	/** The stand by status returned from CPR */
	private boolean checkInStandBy;
	
	/** Stand by security number */
	private String securityNumber;
	
	private boolean seatAutoAssigned;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getCprUniqueCustomerId() {
		return cprUniqueCustomerId;
	}

	public void setCprUniqueCustomerId(String cprUniqueCustomerId) {
		this.cprUniqueCustomerId = cprUniqueCustomerId;
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

	public String getCprProductIdentifierJID() {
		return cprProductIdentifierJID;
	}

	public void setCprProductIdentifierJID(String cprProductIdentifierJID) {
		this.cprProductIdentifierJID = cprProductIdentifierJID;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getOriginalSeatNumber() {
		return originalSeatNumber;
	}

	public void setOriginalSeatNumber(String originalSeatNumber) {
		this.originalSeatNumber = originalSeatNumber;
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

	public boolean isSeatAutoAssigned() {
		return seatAutoAssigned;
	}

	public void setSeatAutoAssigned(boolean seatAutoAssigned) {
		this.seatAutoAssigned = seatAutoAssigned;
	}

}
