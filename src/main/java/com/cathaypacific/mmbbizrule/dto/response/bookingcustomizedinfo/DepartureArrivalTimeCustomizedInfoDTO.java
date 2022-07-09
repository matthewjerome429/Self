package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represent departure or arrival time of a flight.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartureArrivalTimeCustomizedInfoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 879757669745539618L;

	/** Format of time*/
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";
	
	/** time Zone Off set, sample: +0800 */
	private String timeZoneOffset;
	/** the STD in pnr */
	private String pnrTime;

	/** Scheduled time from RTFS (STD or STA in flight status) */
	private String rtfsScheduledTime;
	/** ATD*/
	private String	rtfsActualTime;
	/** Estimated time from RTFS (ETD or ETA in flight status) */
	private String rtfsEstimatedTime;

	public String getPnrTime() {
		return pnrTime;
	}

	public void setPnrTime(String pnrTime) {
		validateTimeFormat(pnrTime);
		this.pnrTime = pnrTime;
	}

	public String getRtfsScheduledTime() {
		return rtfsScheduledTime;
	}

	public void setRtfsScheduledTime(String rtfsScheduledTime) {
		validateTimeFormat(rtfsScheduledTime);
		this.rtfsScheduledTime = rtfsScheduledTime;
	}
	/**
	 * Get Estimated time, will return  rtfs Scheduled time if it not empty, otherwise return pnr time 
	 * @return
	 */
	public String getScheduledTime(){
		return StringUtils.isEmpty(rtfsScheduledTime)?pnrTime:rtfsScheduledTime;
	}
	public String getRtfsEstimatedTime() {
		return rtfsEstimatedTime;
	}

	public void setRtfsEstimatedTime(String rtfsEstimatedTime) {
		validateTimeFormat(rtfsEstimatedTime);
		this.rtfsEstimatedTime = rtfsEstimatedTime;
	}

	public String getRtfsActualTime() {
		return rtfsActualTime;
	}

	public void setRtfsActualTime(String rtfsActualTime) {
		validateTimeFormat(rtfsActualTime);
		this.rtfsActualTime = rtfsActualTime;
	}
 
	/**
	 * Validate whether the time to be set is correct format. null is allowed.
	 * 
	 * @param time
	 *            time to be set.
	 * @exception IllegalArgumentException
	 *                if time is not correct format.
	 */
	private void validateTimeFormat(String time) {
		if (time == null) {
			return;
		}
		try {
			DateUtil.getDateFormat(TIME_FORMAT).parse(time);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Flight time doesn't match format.", e);
		}
	}

	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

}
