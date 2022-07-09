package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

public class NameDTO implements Serializable {

	private static final long serialVersionUID = 9148755192853350818L;
	
	private String givenName;
	
	private String surName;
	
	private String prefix;

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
