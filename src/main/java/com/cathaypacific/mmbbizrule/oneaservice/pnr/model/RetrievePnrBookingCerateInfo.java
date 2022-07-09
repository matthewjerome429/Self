package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.TimeZone;

public class RetrievePnrBookingCerateInfo {
	
	public static final String CREATE_DATE_FOMRAT = "ddMMyy";
	
	public static final String CREATE_TIME_FOMRAT = "HHmm";
	
	public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");
	
	private String createDate;//Create date - ddMMyy
	
	private String createTime;//Create time -hhmm
	 
	private String rpOfficeId;//office Id

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getRpOfficeId() {
		return rpOfficeId;
	}

	public void setRpOfficeId(String rpOfficeId) {
		this.rpOfficeId = rpOfficeId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
