package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response;

import java.io.Serializable;

public class CheckinBaggagePieceDTO implements Serializable {

	private static final long serialVersionUID = 2264329070215146858L;

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
