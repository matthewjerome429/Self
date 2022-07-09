package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.TicketReminderConstant;

public class RetrievePnrTicket {
	
	private String indicator;
	private String date;
	private String time;
	private String officeId;
	
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
	
	public Date getDateTimeByTimezone(String timezoneOffset) {
		Date resultDate = null;
		String dateStr = this.date + (StringUtils.isEmpty(this.time) ? "0000" : this.time);
		try {
			if(StringUtils.isNotEmpty(timezoneOffset)) {
				return DateUtil.getStrToGMTDate(TicketReminderConstant.TIME_FORMAT, timezoneOffset, dateStr);
			} else {
				return DateUtil.getStrToDate(TicketReminderConstant.TIME_FORMAT, dateStr);				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultDate;
	}
	
}
