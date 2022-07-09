package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.io.Serializable;

public class PassengeCustomizedInfoDTO implements Serializable{
	
	private static final long serialVersionUID = -5806418996984531708L;

	/** passenger id */
	private String passengerId;
	
    /** The title. */
    private String title;
    
    /** The family name. */
    private String familyName;
    
    /** The given name. */
    private String givenName;
	
	/** parent id */
	private String parentId;
	
	/** passenger type */
	private String passengerType;
	
	/** FQTV/FQTR of the passenger matched with login member id */
	private Boolean loginFFPMatched; 
	
	/** True if has UMNR ssr*/
	private Boolean unaccompaniedMinor = false;
	
	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
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

	public Boolean getLoginFFPMatched() {
		return loginFFPMatched;
	}

	public void setLoginFFPMatched(Boolean loginFFPMatched) {
		this.loginFFPMatched = loginFFPMatched;
	}

	public Boolean getUnaccompaniedMinor() {
		return unaccompaniedMinor;
	}

	public void setUnaccompaniedMinor(Boolean unaccompaniedMinor) {
		this.unaccompaniedMinor = unaccompaniedMinor;
	}
	
}
