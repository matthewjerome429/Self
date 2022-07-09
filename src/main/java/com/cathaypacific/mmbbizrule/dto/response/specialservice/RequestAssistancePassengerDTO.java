package com.cathaypacific.mmbbizrule.dto.response.specialservice;

import java.io.Serializable;

public class RequestAssistancePassengerDTO implements Serializable {

	private static final long serialVersionUID = -637206299521829352L;
	/** The passenger id. */
    private String passengerId;
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
    /**
     * the primary passenger in the booking,
     * For nun member: it is the login passenger<br>
     * For member: it is member self, if cannot find member self in the booking, it is member's companion in profile
     */
	private Boolean primaryPassenger;
	/**
	 * The Companion passenger
	 */
	private Boolean companion;
	/**
	 * The member self if login as member, else will set null. 
	 */
	private Boolean loginMember;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
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

	public Boolean getPrimaryPassenger() {
		return primaryPassenger;
	}

	public void setPrimaryPassenger(Boolean primaryPassenger) {
		this.primaryPassenger = primaryPassenger;
	}

	public Boolean getCompanion() {
		return companion;
	}

	public void setCompanion(Boolean companion) {
		this.companion = companion;
	}

	public Boolean getLoginMember() {
		return loginMember;
	}

	public void setLoginMember(Boolean loginMember) {
		this.loginMember = loginMember;
	}

}
