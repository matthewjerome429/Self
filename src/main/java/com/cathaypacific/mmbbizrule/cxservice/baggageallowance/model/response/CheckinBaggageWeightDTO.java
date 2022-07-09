package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response;

import java.io.Serializable;

public class CheckinBaggageWeightDTO implements Serializable {

	private static final long serialVersionUID = 5435929578630734658L;

	private int standard;

	private int member;

	private int infant;

	public int getStandard() {
		return standard;
	}

	public void setStandard(int standard) {
		this.standard = standard;
	}

	public int getMember() {
		return member;
	}

	public void setMember(int member) {
		this.member = member;
	}

	public int getInfant() {
		return infant;
	}

	public void setInfant(int infant) {
		this.infant = infant;
	}

}
