package com.cathaypacific.mbcommon.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

public class DateUtil {

	public static final String DATE_PATTERN_DDMMMYY_WITH_SPACE = "dd MMM yy";
	public static final String DATE_PATTERN_YYMMDD_HHMM = "yyMMddHHmm";
	public static final String DATE_PATTERN_DDMMM = "ddMMM";
	public static final String DATE_PATTERN_DDMMYY = "ddMMyy";
	public static final String DATE_PATTERN_DDMMYYYY = "ddMMyyyy";
	public static final String DATE_PATTERN_YYMMDD = "yyMMdd";
	public static final String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_PATTERN_DDMMMYYYY = "ddMMMyyyy";
	public static final String DATE_PATTERN_DDMMMYY = "ddMMMyy";
	public static final String DATE_PATTERN_DDMMMYYHHMM = "ddMMMyyHHmm";
	public static final String DATE_PATTERN_DDMMYYHHMM = "ddMMyyHHmm";
	public static final String DATE_PATTERN_DD_MMM_YY = "dd-MMM-yy";
	public static final String DATE_PATTERN_DD_MMM = "dd MMM";
	public static final String DATE_PATTERN_BOOKINGSUMMARY_FLIGHTDATE = "dd MMM, EEE";
	public static final String TIME_PATTERN_HH_MM = "HH:mm";
	public static final String TIME_PATTERN_H_M = "H:m";
	public static final String TIME_PATTERN_HHMM = "HHmm";
	public static final String DATETIME_PATTERN_YYYYMMDDHHMMSS = "yyyyMMdd HH:mm:ss";
	public static final String DATETIME_PATTERN_YYYYMMDDHHMM = "yyyyMMdd HH:mm";
	public static final String DATE_PATTERN_DDMMYY_HHMM = "ddMMyy HHmm";
	public static final String DATETIME_PATTERN_YYYY_MM_DDHHMM = "yyyy-MM-dd HH:mm";
	public static final String DATETIME_PATTERN_YYYY_MM_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_PATTERN_DD_MM_YYYYHHMMSS = "dd-MM-yyyy HH:mm:ss";
	public static final String DATE_PATTERN_DDMMMYYYY_WITH_SPACE = "dd MMM yyyy";
	public static final String DATE_PATTERN_DDMMMYYYYHHMM = "dd MMM yyyy HH:mm";
	public static final String DATE_PATTERN_DDMMMHHMM = "dd MMM HH:mm";
	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_PATTERN_YYYY = "yyyy";
	public static final String DATE_PATTERN_MM = "MM";
	public static final String DATE_PATTERN_DD = "dd";
	public static final String DATE_PATTERN_EEE = "EEE";
	public static final String DATE_PATTERN_DD_MM_YYYY = "dd-MM-yyyy";
	public static final String DATE_PATTERN_DD_MMM_YYYY_HH_MM = "dd MMM yyyy/HH:mm";
	public static final String TIMEZONE_GMT = "GMT";
	public static final String DATETIME_FORMAT_XML = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String DATE_PATTERN_MM_YY = "MMyy";
	public static final String DATE_PATTERN_MMM_YYYY_WITH_SPACE = "MMM yyyy";
	static ThreadLocal<Map<String, SimpleDateFormat>> sdfTLocal = ThreadLocal.withInitial(() -> new HashMap<>());
	
	/**Current system time millis*/
	private static Long currentTimeMillis = null;
	
	/**
	 * Empty constructor.
	 */
	private DateUtil() {
		
	}
	
	/**
	 * Get current TimeMillis
	 * @return
	 */
    public static final long currentTimeMillis() {
        return currentTimeMillis == null? System.currentTimeMillis():currentTimeMillis;
    }
    /**
     * Get current Date
     * @return
     */
    public static final Date currentDate() {
       return new Date(currentTimeMillis());
    }
    
    /**
     * Get current Calendar
     * @return
     */
    public static final Calendar currentCalendar() {
    	Calendar cla = Calendar.getInstance();
    	cla.setTime(currentDate());
        return cla;
    }
	/**
	 * Each thread is exclusive, creating generic SimpleDateFormat
	 * @param pattern
	 * @return
	 */
	public static SimpleDateFormat getDateFormat(final String pattern) {
		return sdfTLocal.get().computeIfAbsent(pattern, k->new SimpleDateFormat(pattern));
	}

	/**
	 * Convert date text format.
	 * 
	 * @param dateText
	 *            date text.
	 * @param srcFormat
	 *            format of input date text.
	 * @param destFormat
	 *            format of output date text.
	 * @return converted date text.
	 * 
	 * @exception NullPointException
	 *                if srcFormat or destFormat is null.
	 * @exception IllegalArgumentException
	 *                if date format is invalid, or dateText cannot be parsed.
	 */
	public static String convertDateFormat(String dateText, String srcFormat, String destFormat) {
		DateFormat srcDateFormat = getDateFormat(srcFormat);
		DateFormat destDateFormat = getDateFormat(destFormat);

		Date date = null;
		try {
			date = srcDateFormat.parse(dateText);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Date text cannot be parsed by source format.");
		}

		return destDateFormat.format(date);
	}
	
	/**
	 * Convert date text format. will plus 100 years if request time before system time
	 * 
	 * @param dateText
	 *            date text.
	 * @param srcFormat
	 *            format of input date text.
	 * @param destFormat
	 *            format of output date text.
	 * @return converted date text.
	 * 
	 * @exception NullPointException
	 *                if srcFormat or destFormat is null.
	 * @exception IllegalArgumentException
	 *                if date format is invalid, or dateText cannot be parsed.
	 */
	public static String convertDateFormatForExpiryDate(String dateText, String srcFormat, String destFormat) {

		DateFormat srcDateFormat = getDateFormat(srcFormat);
		DateFormat destDateFormat = getDateFormat(destFormat);

		Date date = null;
		try {
			date = srcDateFormat.parse(dateText);
			// plus 100 year if the date is before system time and cannot find yyyy in the request format
			if (date.before(Calendar.getInstance().getTime()) && srcFormat.indexOf(DATE_PATTERN_YYYY) == -1) {
				date = DateUtils.addYears(date, 100);
			}

		} catch (ParseException e) {
			throw new IllegalArgumentException("Date text cannot be parsed by source format.");
		}

		return destDateFormat.format(date);
	}
	
	/**
	 * Convert date text format. will minus 100 years if request time after system time
	 * 
	 * @param dateText
	 *            date text.
	 * @param srcFormat
	 *            format of input date text.
	 * @param destFormat
	 *            format of output date text.
	 * @return converted date text.
	 * 
	 * @exception NullPointException
	 *                if srcFormat or destFormat is null.
	 * @exception IllegalArgumentException
	 *                if date format is invalid, or dateText cannot be parsed.
	 */
	public static String convertDateFormatForDob(String dateText, String srcFormat, String destFormat) {

		DateFormat srcDateFormat = getDateFormat(srcFormat);
		DateFormat destDateFormat = getDateFormat(destFormat);

		Date date = null;
		try {
			date = srcDateFormat.parse(dateText);
			// minus 100 year if the date less then system time and cannot find yyyy in the request format
			if (date.after(Calendar.getInstance().getTime()) && srcFormat.indexOf(DATE_PATTERN_YYYY) == -1) {
				date = DateUtils.addYears(date, -100);
			}

		} catch (ParseException e) {
			throw new IllegalArgumentException("Date text cannot be parsed by source format.");
		}

		return destDateFormat.format(date);
	}
	
	/**
	 * Convert date text format.
	 * 
	 * @param dateText
	 *            date text.
	 * @param srcFormat
	 *            format of input date text.
	 * @param destFormat
	 *            format of output date text.
	 * @return converted date text.
	 * 
	 * @exception NullPointException
	 *                if srcFormat or destFormat is null.
	 * @exception IllegalArgumentException
	 *                if date format is invalid, or dateText cannot be parsed.
	 */
	public static String convertDateTimezone(String dateText, String fromTimezone, String destTimeZone, String timeFormat) {
		
		String resultDate = null;
		try {
			Date fromDate = DateUtil.getStrToDate(timeFormat, dateText, fromTimezone);
			resultDate = DateUtil.getDate2Str(timeFormat, destTimeZone, fromDate);

		} catch (ParseException e) {
			throw new IllegalArgumentException("Date text cannot be parsed by source format.");
		}
		

		return resultDate;
	}

	/**
	 * Convert the date value to the format of given pattern string.
	 * 
	 * @param format
	 *            - Time pattern for this date format.
	 * @param date
	 *            - Date value to be formatted.
	 * @return formatted string date value with given pattern
	 */
	public static String getDate2Str(final String format, final Date date) {
		String result = null;
		if (date != null) {
			DateFormat dateFormat = getDateFormat(format);
			result = dateFormat.format(date);
		}
		return result;
	}
	/**
	 * Convert the date value to the format of given pattern string.
	 * 
	 * @param format
	 *            - Time pattern for this date format.
	 * @param date
	 *            - Date value to be formatted.
	 * @return formatted string date value with given pattern
	 */
	public static String getDate2Str(final String format,final String timeZoneOffSet, final Date date) {
		String result = null;
		if (date != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			TimeZone tz=TimeZone.getTimeZone(ZoneOffset.of(timeZoneOffSet));
			simpleDateFormat.setTimeZone(tz);
			result = simpleDateFormat.format(date);
		}
		return result;
	}
	/**
	 * Convert the string date value to date value.
	 * 
	 * @param format
	 *            - Time pattern for this date format.
	 * @param dateStr
	 *            - String date value to be converted.
	 * @return Date value for the input date string.
	 */
	public static Date getStrToDate(final String format, final String dateStr) throws ParseException {
		Date result = null;
		if (!StringUtils.isEmpty(dateStr)) {
			SimpleDateFormat simpleDateFormat = getDateFormat(format);
			simpleDateFormat.setLenient(false);
			result = simpleDateFormat.parse(dateStr);
		}
		return result;
	}

	/**
	 * Convert the string date value to date value.
	 * 
	 * @param format
	 *            - Time pattern for this date format.
	 * @param dateStr
	 *            - String date value to be converted.
	 * @return Date value for the input date string.
	 */
	public static Date getStrToDate(final String format, final String dateStr, final String timeZoneOffSet)
			throws ParseException {
		Date result = null;
		if (!StringUtils.isEmpty(dateStr)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneOffset.of(timeZoneOffSet)));
			simpleDateFormat.setLenient(false);
			result = simpleDateFormat.parse(dateStr);
		}
		return result;
	}
	/**
	 * Convert the string date value to date value.
	 * 
	 * @param format
	 *            - Time pattern for this date format.
	 * @param dateStr
	 *            - String date value to be converted.
	 * @return Date value for the input date string.
	 */
	public static Date getStrToDate(final String format, final String dateStr, final TimeZone timeZone)
			throws ParseException {
		Date result = null;
		if (!StringUtils.isEmpty(dateStr)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setTimeZone(timeZone);
			simpleDateFormat.setLenient(false);
			result = simpleDateFormat.parse(dateStr);
		}
		return result;
	}
	/**
	 * Method to returns a new current GMT
	 * 
	 * @return current GMT date time
	 */
	public static Date getGMTTime() {
		return Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_GMT), Locale.UK).getTime();
	}

	/**
	 * Convert the string date value to date value.
	 * 
	 * @param format
	 *            - Time pattern for this date format.
	 * @param dateStr
	 *            - String date value to be converted.
	 * @return Date value for the input date string.
	 */
	public static Date getStrToGMTDate(final String format, final String timeZoneOffset, final String dateStr) throws ParseException {
		Date result = null;
		if (!StringUtils.isEmpty(dateStr)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			TimeZone tz=TimeZone.getTimeZone(ZoneOffset.of(timeZoneOffset));
			simpleDateFormat.setTimeZone(tz);
			simpleDateFormat.setLenient(false);
			result = simpleDateFormat.parse(dateStr);
		}
		return result;
	}
	
	/**
	 * Convert Date to Calendar.
	 * 
	 * @param date
	 *            - the Date date.
	 * @return Calendar for the input Date.
	 */
	public static Calendar getDateToCal(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	/**
	 * Convert Date to Calendar.
	 * 
	 * @param date
	 * 			  - the Date date.
	 * @param calendar
	 *            - the int calendar.
	 * @param calendarValue
	 *            - the int calendar value.
	 * @return Calendar for the input Date and calendar.
	 */
	public static Calendar getDateToCal(Date date, int calendar, int calendarValue) {
		Calendar cal = getDateToCal(date);
		cal.add(calendar, calendarValue);
		return cal;
	}
	
	/**
	 * Judge the string year is leap year or not.
	 * 
	 * @param year
	 *            - the string year.
	 * @return wether it is leap year or not for the input year string.
	 */
	public static boolean isLeapYear(int year) {
		if(year%4 == 0 && year%100 != 0 || year%400 == 0){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Compare two date by calendar.
	 * 
	 * @param date1
	 *            - the Date date1.
	 * @param date2
	 *            - the Date date1.
	 * @param calendar
	 *            - the int calendar.
	 * @return the result of comparison between two date.
	 */
	public static int compareDate(Date date1, Date date2, int calendar) {	
		Date d1 = DateUtils.truncate(date1, calendar);
		Date d2 = DateUtils.truncate(date2, calendar);
		return d1.compareTo(d2);
	}
	
	/**
	 * Compare current local zonedTime with the zonedTime transferred by
	 * departureTimeString and departureTimeZoneOffset plus the hours "limithours"
	 * If localCurrentZoneTime > departToLocalZoneTime, return true
	 * Else return false
	 *
	 * @param localCurrentZoneTime
	 * @param departureTimeString
	 * @param departureTimeZoneOffset
	 * @param limithours
	 * @return
	 */
	public static boolean compareBetweenZonedDateTimeByHour(ZonedDateTime localCurrentZoneTime, String departureTimeString, String departureTimeZoneOffset, Integer limithours){
		TimeZone localTimeZone = TimeZone.getDefault();
		TimeZone departTimeZone = TimeZone.getTimeZone(ZoneOffset.of(departureTimeZoneOffset));
		LocalDateTime departureTimeWithoutZone = LocalDateTime.parse(departureTimeString, DateTimeFormatter.ofPattern(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM));
		ZonedDateTime departureZoneTime = departureTimeWithoutZone.atZone(departTimeZone.toZoneId());
		ZonedDateTime departToLocalZoneTime = departureZoneTime.toOffsetDateTime().atZoneSameInstant(localTimeZone.toZoneId());
		departToLocalZoneTime = departToLocalZoneTime.plusHours(limithours);
		Comparator<ZonedDateTime> comparator = Comparator.comparing(zonedDateTime -> zonedDateTime.truncatedTo(ChronoUnit.MINUTES));
		return comparator.compare(localCurrentZoneTime, departToLocalZoneTime) == 1;
	}
	/**
	 * Check the time before system time or not 
	 * @param dateStr
	 * @param format
	 * @param timeZoneOffSet
	 * @return
	 * @throws ParseException
	 */
	public static boolean beforeSystemTime(String dateStr, String format, String timeZoneOffSet) throws ParseException {
		Date date = getStrToDate(format, dateStr, timeZoneOffSet);
		if (date != null) {
			return date.getTime() <= System.currentTimeMillis();
		} else {
			return false;
		}

	}
	
	/**
	 * Check the time whether before system time(not include today: today is after systemTime) 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static boolean beforeSystemTime(String dateStr, String format){
		Date date;
		try {
			date = getStrToDate(format, dateStr);
			if(date != null){
			     Calendar rightNow = Calendar.getInstance();  
			     rightNow.setTime(new Date());  
			     rightNow.add(Calendar.DAY_OF_MONTH, -1); 
				return date.before(rightNow.getTime());
			} else {
				return false;
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Date text:" + dateStr + "cannot be parsed by source format:" + format);
		}
	}
	
    /**
     * Retrieve day of week from a date object
     * @param date
     * @return day of week e.g. Mon, Tue, etc.
     */
    public static String getDayOfWeek(Date date) {
    	return getDateFormat("EEE").format(date);
    }
    
    /**
     * Replace day of week 'EEE' by a specified day of week value
     * @param dateFormat
     * @param dayOfWeek
     * @return replaced date format string
     */
    public static String replaceDayOfWeek(String dateFormat, String dayOfWeek) {
    	if(!StringUtils.isEmpty(dateFormat)) {
    		dateFormat = dateFormat.replaceAll("EEE", "'" + dayOfWeek + "'");
    	}
    	return dateFormat;
    }
    
    /**
     * Calculate date offset
     * @param from
     * @param to
     * @return number of day offset in int format (e.g. -1, 0, 1, 2)
     */
    public static int calDateOffset(Date from, Date to) {
	   Date fromInDate = resetTime(from);
	   Date toInDate = resetTime(to);
	   double difference = toInDate.getTime() - fromInDate.getTime();
	   double daysBetween = (difference / (1000*60*60*24));
	   return (int) daysBetween;
    }
    
    /**
     * Reset time in date object
     * @param date
     * @return Date
     */
    public static Date resetTime(Date date) {
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(date);
       calendar.set(Calendar.HOUR_OF_DAY, 0);
       calendar.set(Calendar.MINUTE, 0);
       calendar.set(Calendar.SECOND, 0);
       calendar.set(Calendar.MILLISECOND, 0);
 	   return calendar.getTime();
    }
    
    /**
     * get UTC Calendar
     * 
     * @return Calendar
     */
    public static Calendar getUTCCalendar() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal;
    }
    
    /**
     * Convert the Calendar value to the format of given pattern string.
     * 
     * @param cal
     * @param format
     * @return String
     */
    public static String getCal2Str(Calendar cal, String format) {
    	StringBuilder utcTimeBuilder = new StringBuilder();
    	int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE); 
        utcTimeBuilder.append(year).append("-").append(month).append("-").append(day).append(" ").append(hour).append(":").append(minute);
        return convertDateFormat(utcTimeBuilder.toString(), DATETIME_PATTERN_YYYY_MM_DDHHMM, format);   
    }
    
    /**
     * Convert the current Calendar value to the format of given pattern string.
     * 
     * @param format
     * @return String
     */
    public static String getCurrentCal2Str(String format) {
    	return getCal2Str(getUTCCalendar(), format);
    }
    
    /**
     * get the difference hours between date1 and date2
     * @param date1
     * @param date2
     * @return 
     */
    public static int getDifferenceHours(Date date1, Date date2) {
    	return (int) ((date1.getTime() - date2.getTime()) / (1000*3600));
    }
    
    /**
     * get SimpleDateFormat by timeZone & format
     * 
     * @param timeZone
     * @param format
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(TimeZone timeZone, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(timeZone);
		return sdf;
    }

	public static void setCurrentTimeMillis(Long currentTimeMillis) {
		DateUtil.currentTimeMillis = currentTimeMillis;
	}
	
	/**
	 * Get SPBP Generated GMT Time
	 * @return
	 */
	public static String getGeneratedGMTTime() {
		Date generatedDate = new Date();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone(DateUtil.TIMEZONE_GMT));
		
		return dateFormat.format(generatedDate);
	}
}
