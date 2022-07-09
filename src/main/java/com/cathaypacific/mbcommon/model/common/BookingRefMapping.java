package com.cathaypacific.mbcommon.model.common;

import org.apache.commons.lang.StringUtils;

public class BookingRefMapping {
	
	/**
	 * other airLine rloc.
	 */
	private String oalRloc;
	
	private String oneARloc;
	
	private String ojRloc;
	
	private Passenger primaryPax;

	public String getOalRloc() {
		return oalRloc;
	}

	public void setOalRloc(String oalRloc) {
		this.oalRloc = oalRloc;
	}

	public String getOneARloc() {
		return oneARloc;
	}

	public void setOneARloc(String oneARloc) {
		this.oneARloc = oneARloc;
	}

	public String getOjRloc() {
		return ojRloc;
	}

	public void setOjRloc(String ojRloc) {
		this.ojRloc = ojRloc;
	}
	
	public boolean oneARlocExists(String oneARloc) {
		if(StringUtils.isBlank(oneARloc)) {
			return false;
		}
		return oneARloc.equals(this.oneARloc);
	}
	
	public boolean oneOalRlocExists(String oalRloc) {
		if(StringUtils.isBlank(oalRloc)) {
			return false;
		}
		return oalRloc.equals(this.oalRloc);
	}
	
	public void resetOalRloc(String oalRloc) {
		if(StringUtils.isBlank(oalRloc)) {
			return;
		}
		setOalRloc(oalRloc);
	}
	
	public void resetOjRloc(String ojRloc) {
		if(StringUtils.isBlank(ojRloc)) {
			return;
		}
		setOjRloc(ojRloc);
	}

	public Passenger getPrimaryPax() {
		return primaryPax;
	}

	public void setPrimaryPax(Passenger primaryPax) {
		this.primaryPax = primaryPax;
	}
	
}
