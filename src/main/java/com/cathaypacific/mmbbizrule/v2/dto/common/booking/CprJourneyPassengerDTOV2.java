package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CprJourneyPassengerDTOV2 extends BaseResponseDTO {

	private static final long serialVersionUID = 4951984273949137266L;

	private String passengerId;
	
	/** Passenger Unique Customer ID, UCI, from CPR */
	private String cprUniqueCustomerId;
	
	private Boolean canCheckIn;

	/** inhibit BP (field for sent boarding pass)*/
	private boolean inhibitBP;
	
	/** Indicate whether Mobile Boarding Pass is allowed **/
	private boolean allowMBP;
	
	/** Indicate whether Self Print Boarding Pass is allowed **/
	private boolean allowSPBP;
	
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

	public Boolean getCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(Boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
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
	
}
