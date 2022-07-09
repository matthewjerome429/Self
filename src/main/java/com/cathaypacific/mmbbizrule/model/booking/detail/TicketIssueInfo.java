package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.io.Serializable;

public class TicketIssueInfo implements Serializable{

	private static final long serialVersionUID = 8910187791430083955L;

	private String indicator;
	
	private String date;
	
	private String time;
	
	private String officeId;
	
	private String timeZoneOffset;
	
	private String rpLocalDeadLineTime;

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getRpLocalDeadLineTime() {
		return rpLocalDeadLineTime;
	}

	public void setRpLocalDeadLineTime(String rpLocalDeadLineTime) {
		this.rpLocalDeadLineTime = rpLocalDeadLineTime;
	}
	
}
