package com.cathaypacific.mmbbizrule.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MMBBizruleConstants {
 
	
	/** flight number standard length */
	public static final int FLIGHT_NUMEBR_STANDARD_LENGTH = 4;
	
	/** special country number */
	public static final String SPECIAL_COUNTRY_NUMBER = "1";
	/** special country number with + */
	public static final String SPECIAL_COUNTRY_NUMBER_WITH_PREFIX = "+1";
	/** use this country while country number is 1 and no area code is found */
	public static final String COUNTRY_CODE_FOR_NO_AREA_CODE_MAPPED = "US";
	/** mobile phone type */
	public static final String PHONE_TYPE_MOBILE = "M";
	
	/** cabin class */
	public static final String CABIN_CLASS_FIRST_CLASS = "F";
	public static final String CABIN_CLASS_BUSINESS_CLASS = "J";
	public static final String CABIN_CLASS_PEY_CLASS = "W";
	public static final String CABIN_CLASS_ECON_CLASS = "Y";
	
	
    public static final String PAX_CHECKIN_INDICATOR_Y = "Y";
    /** The Constant IBE_PATTERN_KA. */
    public static final String BOH_OFFICEID_PATTERN = ".{3,3}(KA|CX)08.{2,2}";
    
    /** Key name of the provided key file */
    public static final String IBE_KEY = "ibe";
    public static final String MB_KEY = "cxmb";
    
    /** Gender */
    public static final String GENDER_MALE = "M";
    
    public static final String GENDER_FEMALE = "F";
    
    /** OLCI */
	public static final String OLCI_SOURCE= "1ADCS";
	public static final String ERR_MMB_DISPLAYONLY = "ERR_MMB_DISPLAYONLY";
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String[] NAME_TITLE = { "MS", "MR", "MSTR", "MRS", "MISS", "DR", "PROF", "REV", "SIR" };
    
    /** Flight number range */
    public static final int FLIGHT_NUMBER_MIN = 0;
    public static final int FLIGHT_NUMBER_MAX = 9999;
    
    public static final String ASR_ENABLE_SUBCLASS_X = "X";

	public static final String BOOKING_TYPE_HOTEL = "Hotel";
	public static final String BOOKING_TYPE_EVENT = "Event";
	public static final String BOOKING_TYPE_FLIGHT = "Flight";
	public static final String BOOKING_TYPE_PACKAGE = "Package";
	
	/** Cabin subclass */
	public static final String CABIN_SUBCLASS_O = "O";
	public static final String CABIN_SUBCLASS_T = "T";
	
	
	//public static final List<String> OPERATE_COMPANYS = Arrays.asList("CX","KA");
	public static final String KA_OPERATOR="KA";
	public static final String CX_OPERATOR="CX";
	
	/** POS */
	public static final String POS_CHINA = "CN";
	public static final String POS_INDIA = "IN";
	
	/** Country code */
	public static final String COUNTRY_CODE_JAPAN = "JP";
	public static final String COUNTRY_CODE_INDIA = "IN";
	
	/** Special characters */
	public static final String SPECIAL_CHARACTERS = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
	
	/** Booking add result */
	/** Booking added by adding FQTV */
	public static final String BOOKING_ADD_RESULT_FQTV = "FQTV";
	/** Booking added by adding CUST */
	public static final String BOOKING_ADD_RESULT_CUST = "CUST";
	/** Need confirm for RU member */
	public static final String BOOKING_ADD_RESULT_NEED_CONFIRM = "NC";
	
	/** Update seat reject reason */
	/** Ineligible to update seat */
	public static final String UPDATE_SEAT_REJECTED_INELIGIBLE = "INELIGIBLE";
	
	/** Country code */
	public static final String COUNTRY_CODE_USA = "US";
	public static final String COUNTRY_CODE_NEWZEALAND = "NZ";
	public static final String COUNTRY_CODE_HONGKONG = "HK";
	public static final String COUNTRY_CODE_USA_THREE = "USA";
	
	/** Tiers with additional member baggage allowance */
	public static final List<String> TIERS_WITH_MEMBER_BAGGAGE_ALLOWANCE = Collections.unmodifiableList(Arrays.asList("IN","DM","DMP","GO","SL","GR","SAPP","EMER"));
	
	public static final String BLOKCED_STAFF_PRI_CODE="01";
	
	/** Purchase history seat type */
	public static final String SEAT_TYPE_EXTRA_LEGROOM = "EXL";
	public static final String SEAT_TYPE_ASR_REGULAR = "ASR_REGULAR";
	public static final String SEAT_TYPE_ASR_WINDOW = "ASR_WINDOW";
	public static final String SEAT_TYPE_ASR_AISLE = "ASR_AISLE";

	/** Export related */
	public static final String EXPORT_CALENDAR_FILE_PREFIX = "Calendar_";
	public static final String EXPORT_CALENDAR_FILE_SUFFIX = ".ics";
	public static final String EXPORT_CALENDER_LINESEPARATOR = "   \n";
	public static final String EXPORT_CALENDAR_DESCRIPTION_TITLE = "My Cathay Pacific Booking Details";
	public static final String EXPORT_CALENDAR_DESCRIPTION_BOOKING_REFERENCE = "Booking Reference: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_CHECK_IN_LINK = "Online Check-In: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_MMB_LINK = "Manage Booking: ";
	
	public static final String EXPORT_CALENDAR_DESCRIPTION_FLIGHT_NO = "Flight No.: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_ORIGIN = "Origin: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_DESTIONATION = "Destination: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_DEPART = "Depart: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_ARRIVE = "Arrive: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_CLASS = "Class: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_CHECK_IN_TIME = "Online Check-In available on: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_CHECK_IN_REMINDER = "*** Check your flight departure time online before going to the airport ***";
	
	public static final String EXPORT_CALENDAR_DESCRIPTION_HOTEL_NAME = "Hotel Name: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_HOTEL_CITY = "Hotel City: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_HOTEL_CHECK_IN_DATE = "Check-in date: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_HOTEL_CHECK_IN_TIME = "Check-in time: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_HOTEL_CHECK_OUT_DATE = "Check-out date: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_HOTEL_CHECK_OUT_TIME  = "Check-out time: ";
	
	public static final String EXPORT_CALENDAR_DESCRIPTION_EVENT_NAME = "Event/Travel Extra Name: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_EVENT_CITY_NAME = "Event/Travel Extra City: ";
	public static final String EXPORT_CALENDAR_DESCRIPTION_EVENT_DATE = "Date: ";
	
	public static final String NON_AIR_SECTOR_CARRIER_CODE_DEUTSCHE_BAHN = "9B";
	
	public static final String HAVE_UNACCOMPANIED_MINOR_EFORM = "withUMForm";
	public static final String NON_UNACCOMPANIED_MINOR_EFORM = "withoutUMForm";
	public static final String MMB_STATUS_REQUEST = "RQ";
	
	public static final String CHATBOT_FLOW_ATCI = "ATCI";
	
	public static final String OFFICIAL_RECEIPT_PDF_TEMPLATE_PATH = "/pdf/Official_Receipt_Template.pdf";
	
	public static final List<String> CLS_EMAIL_RIGISTERED_ERRORS = Collections.unmodifiableList(Arrays.asList("CLSAPI_CFRX_21_9088", "CLSAPI_CFRX_22_8243"));
	
    public static final String SPECIAL_SERVICE_STATUS_CONFIRM = "CF";
    
    /** RU enrollment */
    public static final String ENROL_REQUEST_NATION_CODE = "CXPROMO";
    public static final String ENROL_REQUEST_SELECTION_OPTION_CODE_SELECTED = "Y";
    public static final String ENROL_REQUEST_SELECTION_OPTION_CODE_NOT_SELECTED = "N";
    public static final String ENROL_REQUEST_CHANNEL_CODE = "EMAIL";
    public static final String ENROL_REQUEST_ENROLMENT_SOURCE = "CX006";
    public static final String CLS_STATUS_CODE_SUCCESS = "00";
    
	/** WCxx SSR which makes the pax eligible to select regular seat for free */
	public static final List<String> ELIGIBLE_WC_SSR_LIST_OF_ASR_FOC = Collections.unmodifiableList(Arrays.asList("WCHR", "WCHS", "WCBD", "WCBW", "WCMP", "WCOB"));
	
	public static final String APPLICATION_TOKEN_DATA_CACHE_KEY_ONE_A_RLOC = "one_a_rloc";
	
	/** update type*/
	public static final String UPDATE_TYPE_CONTACT_INFO = "contactInfo";
	
	/** update type*/
	public static final String UPDATE_TYPE_FFP = "ffp";
	
	/** Flight Status facilities */
	public static final String FACILITY_WIFI = "wifi";
	
	/** error field name */
	public static final String ERROR_FIELD_NAME_READ_TIME_OUT = "READ_TIME_OUT";
	
	/** Seat payment status from 1A */
	public static final String SEAT_PAYMENT_STATUS_PAID = "PAD";
	public static final String SEAT_PAYMENT_STATUS_WAIVED = "WAV";
	public static final String SEAT_PAYMENT_STATUS_EXEMPTED = "EXM";
	public static final String SEAT_PAYMENT_STATUS_NO_NEED_TO_PAY = "NA";
	
	public static final String ACCESS_CHANNEL_WEB = "WEB";
	
	public static final List<String> SPECIAL_SERVICE_MOBILITY_CODE = Collections.unmodifiableList(Arrays.asList("WCHS", "WCHR", "WCHC", "WCLB", "WCBD", "WCBW", "WCMP", "WCOB"));
	public static final List<String> SPECIAL_SERVICE_VISUAL_CODE = Collections.unmodifiableList(Arrays.asList("BLND"));
	public static final List<String> SPECIAL_SERVICE_HEARING_CODE = Collections.unmodifiableList(Arrays.asList("DEAF"));
	public static final List<String> SPECIAL_SERVICE_AIRPORT_ASSISTANCE_CODE = Collections.unmodifiableList(Arrays.asList("MAAS"));

    public static final String MEAL_ELIGIBILITY_JOBID = "jobId";
    
	public static final String CANCELLED_SEGMENT_SUFFIX = "C";

	private MMBBizruleConstants(){
    	
    }
}