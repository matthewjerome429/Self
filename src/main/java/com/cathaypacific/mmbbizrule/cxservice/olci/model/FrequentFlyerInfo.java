package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class FrequentFlyerInfo implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -7316111077955850742L;

	/**
	 * Frequent Flyer Carrier
	 */
	private String carrier;

	/**
	 * Frequent Flyer Number
	 */
	private String number;

	/**
	 * Frequent Flyer Tier Level
	 */
	private String tierLevel;

	/**
	 * Frequent Priority Code
	 */
	private String priorityCode;

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTierLevel() {
		return tierLevel;
	}

	public void setTierLevel(String tierLevel) {
		this.tierLevel = tierLevel;
	}

	public String getPriorityCode() {
		return priorityCode;
	}

	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}
}
