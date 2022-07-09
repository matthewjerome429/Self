package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response;

import java.io.Serializable;

public class CabinSmallItemDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5607256674717615742L;

	private int standard;

	private int member;

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

}
