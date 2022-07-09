package com.cathaypacific.mmbbizrule.dto.response.checkin.cancancel;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CanCancelCheckInPassengerDTO extends BaseResponseDTO {

	private static final long serialVersionUID = -3683014573035329047L;
	
	/** The passenger id. */
	private String passengerId;
	
	/** Passenger Unique Customer ID, UCI, from CPR */
	private String cprUniqueCustomerId;
	
	/** If The passenger is infant without seat, its value will be his parentId. */
	private String parentId;
	
	/** The passenger type. */
	private String passengerType;
	
	/** The title. */
    private String title;
    
    /** The family name. */
    private String familyName;
    
    /** The given name. */
    private String givenName;
    
    /** The passenger is checked-in */
    private boolean checkedIn;
    
    /** The passenger can cancel check-in or not */
    private boolean canCancelCheckIn;
    
    /** The passenger is login passenger or not */
    private boolean loginPassenger;
    
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
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

	public boolean isLoginPassenger() {
		return loginPassenger;
	}

	public void setLoginPassenger(boolean loginPassenger) {
		this.loginPassenger = loginPassenger;
	}

	public boolean isCheckInStandBy() {
		return checkInStandBy;
	}

	public void setCheckInStandBy(boolean checkInStandBy) {
		this.checkInStandBy = checkInStandBy;
	}
    
}
