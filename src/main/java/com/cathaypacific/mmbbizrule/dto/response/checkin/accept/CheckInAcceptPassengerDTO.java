package com.cathaypacific.mmbbizrule.dto.response.checkin.accept;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CheckInAcceptPassengerDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 8485051961726128256L;
	
	private String passengerId;
	
	/** Passenger Unique Customer ID, UCI, from CPR */
	private String cprUniqueCustomerId;
	
	private boolean checkedIn;
	
	/** Indicate boarding pass is sent */
 	private Boolean boardingPassSent = false;

	/** inhibit BP (field for sent boarding pass)*/
 	@JsonIgnore
	private boolean inhibitBP = false;
 	
 	private boolean checkInStandBy;
 	
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

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	public Boolean getBoardingPassSent() {
		return boardingPassSent;
	}

	public void setBoardingPassSent(Boolean boardingPassSent) {
		this.boardingPassSent = boardingPassSent;
	}

	public boolean isInhibitBP() {
		return inhibitBP;
	}

	public void setInhibitBP(boolean inhibitBP) {
		this.inhibitBP = inhibitBP;
	}

	public boolean isCheckInStandBy() {
		return checkInStandBy;
	}

	public void setCheckInStandBy(boolean checkInStandBy) {
		this.checkInStandBy = checkInStandBy;
	}
}
