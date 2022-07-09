package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

public class ContactDetailsDTO implements Serializable {

	private static final long serialVersionUID = 5765535388017334327L;
	
	private NameDTO name;

	public NameDTO getName() {
		return name;
	}

	public void setName(NameDTO name) {
		this.name = name;
	}
	
}
