package com.cathaypacific.mmbbizrule.business;

import java.text.ParseException;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;

import net.fortuna.ical4j.model.Calendar;

public interface ExportBusiness {

	/**
	 * get trip summary calendar
	 * 
	 * @param oneARloc
	 * @param ojRloc
	 * @param loginInfo
	 * @return Calendar
	 * @throws BusinessBaseException
	 * @throws ParseException
	 */
	public Calendar getTripSummaryCal(String oneARloc, String ojRloc, LoginInfo loginInfo) throws BusinessBaseException, ParseException;
	
}
