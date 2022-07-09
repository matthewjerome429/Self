package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class TravelDocGroup implements Serializable {

	private static final long serialVersionUID = 6961796401256568045L;

	private String name;

	private boolean mandatory;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.mandatory = isMandatory;
	}
}
