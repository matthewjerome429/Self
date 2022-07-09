package com.cathaypacific.mmbbizrule.constant;

public class OLCIConstants {
	
	private OLCIConstants() {
	}
	public static final String CPR_ERROR_INFO_TYPE_D = "D";
	public static final String CPR_ERROR_INFO_TYPE_L = "L";
	public static final String CPR_ERROR_INFO_TYPE_W = "W";
	public static final String CPR_ERROR_INFO_TYPE_S = "S";
	public static final String CPR_ERROR_INFO_TYPE_B = "B";
	
	public static final String CPR_PASSENGER_CAN_CHECKIN_EXCEPT_EXCEPTION_CODE = "E13Z00119";
	public static final String CPR_JOURNEY_NOT_YET_CHECKIN_ERROR_CODE = "E12Z00151";
	public static final String CPR_JOURNEY_NEED_UPGRADE_TO_CHECK_IN_ERROR_CODE = "E12Z00152";
	public static final String CPR_PASSENGER_PARTIAL_ACCPENTED_JOURNEY = "E13Z00260";

	public static final String CPR_ERROR_FIELD_NAME_REJECT = "REJECTED";
	public static final String CPR_ERROR_FIELD_NAME_STANDBY = "STANDBY";

	public static final String ERR_ADC_FOUND = "E12Z00247";
	public static final String WAR_ADC_FOUND = "W12Z00248";
	public static final String WAR_ADC_BP_NOTALLOW = "W12Z00256";

	public static final String PAX_TYPE_NON_MEMBER = "C";
	
	public static final String CUSTOMERLEVEL_PAX_TYPE_ADULT = "A";
    public static final String CUSTOMERLEVEL_PAX_TYPE_CHILDREN = "C";
    public static final String CUSTOMERLEVEL_PAX_TYPE_INFANT = "IN";
    public static final String CUSTOMERLEVEL_PAX_TYPE_INFANT_SEAT = "767";
    public static final String CUSTOMERLEVEL_PAX_TYPE_BAGGAGE = "B";
    public static final String CUSTOMERLEVEL_PAX_TYPE_EXTRA = "E";
}
