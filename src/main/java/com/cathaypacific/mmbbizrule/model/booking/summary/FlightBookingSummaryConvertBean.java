package com.cathaypacific.mmbbizrule.model.booking.summary;

public class FlightBookingSummaryConvertBean {
	
	/**
	 * rloc 
	 */
	private String rloc;

	/**
	 * This booking can get from 1A member search y memebr id
	 */
	private boolean inOneA = false;
	
	/**
	 * This booking can get from EODS by memberId
	 */
	private boolean inEods = false;
	
	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public boolean isInOneA() {
		return inOneA;
	}

	public void setInOneA(boolean inOneA) {
		this.inOneA = inOneA;
	}

	public boolean isInEods() {
		return inEods;
	}

	public void setInEods(boolean inEods) {
		this.inEods = inEods;
	}

	
}
