package com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.request;

public class CancelBookingEligibilityRequest {

	public static final String CREATION_DATE_FORMAT = "yyyyMMddHHmm"; 
	
	private String rloc;
	/**
	 * format yyyyMMddhh 
	 * (yyyyMMddhh24mi) (GMT+8) e.g. 201902141200
	 */
	private String creationDate;
	
	public String getRloc() {
		return rloc;
	}
	public void setRloc(String rloc) {
		this.rloc = rloc;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	} 
	
	
}
