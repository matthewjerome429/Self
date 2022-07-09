package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

public class RoomGuestDetailDTO implements Serializable{

	private static final long serialVersionUID = -6791984118491949062L;
	
	private String givenName;
	
	private String middleName;
	
	private String surname;
	
	private String paxType;
	
	private String prefix;

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPaxType() {
		return paxType;
	}

	public void setPaxType(String paxType) {
		this.paxType = paxType;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
