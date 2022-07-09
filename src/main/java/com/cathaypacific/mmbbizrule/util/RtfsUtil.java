package com.cathaypacific.mmbbizrule.util;

import java.text.ParseException;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.utils.DateUtil;

public class RtfsUtil {

	/**  -3 * 24 * 60 * 60 * 1000 */
	private static long rtfsStartLimit = -259200000;
	
	/** 4 * 24 * 60 * 60 * 1000*/
	private static long rtfsEndLimit = 345600000;

	private RtfsUtil() {
		// do nothing
	}

	/**
	 * 
	 * @param departureTime
	 *            format yyyy-MM-dd HH:mm
	 * @param timeZoneOffset
	 *            e.g. +800
	 * @return
	 * @throws ParseException
	 */
	public static boolean inRtfsTimeframe(String departureTime, String dateFormat, String timeZoneOffset)
			throws ParseException {
	
		if (StringUtils.isEmpty(departureTime) || StringUtils.isEmpty(dateFormat)) {
			return false;
		}
		
		long systemTime = System.currentTimeMillis();
		
		if (!StringUtils.isEmpty(timeZoneOffset)) {
			Date departureGMTTime = DateUtil.getStrToGMTDate(dateFormat, timeZoneOffset, departureTime);
			if ((departureGMTTime.getTime() - systemTime) >= rtfsStartLimit
					&& (departureGMTTime.getTime() - systemTime) <= rtfsEndLimit) {
				return true;
			}
		}
		return false;
	}
}
