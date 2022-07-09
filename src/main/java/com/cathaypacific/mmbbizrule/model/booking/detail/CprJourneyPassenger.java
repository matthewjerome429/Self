package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.util.List;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class CprJourneyPassenger {
	
	private String passengerId;
	
	/** Passenger Unique Customer ID, UCI, from CPR */
	private String cprUniqueCustomerId;
	
	private boolean displayOnly;
	
	private boolean canCheckIn;
	
	/** inhibit BP (field for sent boarding pass)*/
	private boolean inhibitBP;
	
	/** Indicate whether Mobile Boarding Pass is allowed **/
	private boolean allowMBP;
	
	/** Indicate whether Self Print Boarding Pass is allowed **/
	private boolean allowSPBP;
	
	private boolean checkedIn;
	
	private boolean canCancelCheckIn;
	
	private List<ErrorInfo> errors;
	
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

	public boolean isInhibitBP() {
		return inhibitBP;
	}

	public void setInhibitBP(boolean inhibitBP) {
		this.inhibitBP = inhibitBP;
	}

	public boolean isAllowMBP() {
		return allowMBP;
	}

	public void setAllowMBP(boolean allowMBP) {
		this.allowMBP = allowMBP;
	}

	public boolean isAllowSPBP() {
		return allowSPBP;
	}

	public void setAllowSPBP(boolean allowSPBP) {
		this.allowSPBP = allowSPBP;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	public boolean isCanCancelCheckIn() {
		return canCancelCheckIn;
	}

	public void setCanCancelCheckIn(boolean canCancelCheckIn) {
		this.canCancelCheckIn = canCancelCheckIn;
	}

	public boolean isCheckInStandBy() {
		return checkInStandBy;
	}

	public void setCheckInStandBy(boolean checkInStandBy) {
		this.checkInStandBy = checkInStandBy;
	}

}
