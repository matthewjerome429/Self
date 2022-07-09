package com.cathaypacific.mmbbizrule.dto.response.officialreceipt;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OfficialReceiptPassengerDTO implements Serializable {
	
	private static final long serialVersionUID = -5920506094878716608L;

	private String passengerId;
	
	private String passengerType;
	
	private String parentId;
	
	private String familyName;
	
	private String givenName;
	
	private Boolean eligible;
	
	private Boolean login;
	
	@JsonIgnore
	private Boolean isCxKaTicket;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public Boolean isEligible() {
		return eligible;
	}

	public void setEligible(Boolean eligible) {
		this.eligible = eligible;
	}

	public Boolean isLogin() {
		return login;
	}

	public void setLogin(Boolean login) {
		this.login = login;
	}

	public Boolean isCxKaTicket() {
		return isCxKaTicket;
	}

	public void setIsCxKaTicket(Boolean isCxKaTicket) {
		this.isCxKaTicket = isCxKaTicket;
	}
	
}
