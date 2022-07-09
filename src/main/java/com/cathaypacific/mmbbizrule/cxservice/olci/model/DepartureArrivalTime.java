package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;
import java.text.ParseException;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represent departure or arrival time of a flight.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartureArrivalTime implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 879757669745539618L;

	/** Format of time */
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";

//	/** Date format */
//	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(TIME_FORMAT);

	/** time Zone Off set, sample: +0800 */
	private String timeZoneOffset;

	/** Scheduled time from CPR (STD or STA in 1A) */
	private String cprScheduledTime;

	/** Estimated time from CPR (ETD or ETA in 1A) */
	private String cprEstimatedTime;

	/** Scheduled time from RTFS (STD or STA in flight status) */
	private String rtfsScheduledTime;

	/** Estimated time from RTFS (ETD or ETA in flight status) */
	private String rtfsEstimatedTime;

	public String getCprScheduledTime() {
		return cprScheduledTime;
	}

	public void setCprScheduledTime(String cprScheduledTime) {
		validateTimeFormat(cprScheduledTime);
		this.cprScheduledTime = cprScheduledTime;
	}

	public String getCprEstimatedTime() {
		return cprEstimatedTime;
	}

	public void setCprEstimatedTime(String cprEstimatedTime) {
		validateTimeFormat(cprEstimatedTime);
		this.cprEstimatedTime = cprEstimatedTime;
	}

	public String getRtfsScheduledTime() {
		return rtfsScheduledTime;
	}

	public void setRtfsScheduledTime(String rtfsScheduledTime) {
		validateTimeFormat(rtfsScheduledTime);
		this.rtfsScheduledTime = rtfsScheduledTime;
	}

	public String getRtfsEstimatedTime() {
		return rtfsEstimatedTime;
	}

	public void setRtfsEstimatedTime(String rtfsEstimatedTime) {
		validateTimeFormat(rtfsEstimatedTime);
		this.rtfsEstimatedTime = rtfsEstimatedTime;
	}

	/**
	 * Get effective value of this time.
	 * <p>
	 * Because every kinds of value indicate same time, only one is effective.
	 * 
	 * @return effective time. If no kind of time exists, return null.
	 */
	public String getTime() {
		String time = null;
		if (rtfsEstimatedTime != null) {
			time = rtfsEstimatedTime;
		} else if (rtfsScheduledTime != null) {
			time = rtfsScheduledTime;
		} else if (cprEstimatedTime != null) {
			time = cprEstimatedTime;
		} else if (cprScheduledTime != null) {
			time = cprScheduledTime;
		}
		return time;
	}

	/**
	 * Get effective value of this time in STD.
	 * <p>
	 * Because every kinds of value indicate same time, only one is effective.
	 * 
	 * @return effective time. If no kind of time exists, return null.
	 */
	public String getScheduledTime() {
		String time = null;
		if (rtfsScheduledTime != null) {
			time = rtfsScheduledTime;
		} else if (cprScheduledTime != null) {
			time = cprScheduledTime;
		}
		return time;
	}

	/**
	 * Get effective value of this time in ETD.
	 * <p>
	 * Because every kinds of value indicate same time, only one is effective.
	 * 
	 * @return effective time. If no kind of time exists, return null.
	 */
	public String getEstimatedTime() {
		String time = null;
		if (rtfsEstimatedTime != null) {
			time = rtfsEstimatedTime;
		} else if (cprEstimatedTime != null) {
			time = cprEstimatedTime;
		}
		return time;
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
