package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.text.ParseException;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.dto.common.GroupBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represent departure or arrival time of a flight.
 *
 */
public class DepartureArrivalTimeDTOV2 extends GroupBaseDTO implements Serializable {

	private static final long serialVersionUID = 879757669745539618L;

	/** Format of time*/
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";

//	/** Date format */
//	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(TIME_FORMAT);
	
	/** time Zone Off set, sample: +0800 */
	private String timeZoneOffset;

	/** Scheduled time from RTFS (STD or STA in flight status) */
	private String rtfsScheduledTime;
	
	/** ATD*/
	private String rtfsActualTime;
	
	/** Estimated time from RTFS (ETD or ETA in flight status) */
	private String rtfsEstimatedTime;
	
	private String pnrTime;

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

	public String getRtfsActualTime() {
		return rtfsActualTime;
	}

	public void setRtfsActualTime(String rtfsActualTime) {
		validateTimeFormat(rtfsActualTime);
		this.rtfsActualTime = rtfsActualTime;
	}

	/**
	 * Get effective value of this time.
	 * <p>
	 * Because every kinds of value indicate same time, only one is effective.
	 * 
	 * @return effective time. If no kind of time exists, return null.
	 */
	@JsonIgnore
	public String getTime() {
		String time = pnrTime;
		if(rtfsActualTime != null){
			time = rtfsActualTime;
		}else if(rtfsEstimatedTime != null) {
			time = rtfsEstimatedTime;
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

	public String getPnrTime() {
		return pnrTime;
	}

	public void setPnrTime(String pnrTime) {
		this.pnrTime = pnrTime;
	}

}
