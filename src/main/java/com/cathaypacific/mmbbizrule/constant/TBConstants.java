package com.cathaypacific.mmbbizrule.constant;

public class TBConstants {
	
	private TBConstants(){
		
	}
	
	public static final String APP_CODE = "MMB";
	
	public static final String APP_CODE_WILDCARD = "*";
	
	public static final String BOOKINGSTATUS_DISABLE = "DISABLED";
	
	public static final String BOOKINGSTATUS_ENABLED = "ENABLED";
	
	public static final String TRAVEL_DOC_WILDCARD   = "**";
	
	public static final String TRAVEL_DOC_PRIMARY = "PT";
	
	public static final String TRAVEL_DOC_SECONDARY = "ST";
	
	public static final String SSR_TRAVEL_DOC_AVAILABLE = "AT";
	
	
	/**field TRAVEL_DOC_DISPLAY in table TB_TRAVEL_DOC_DISPLAY */
	public static final String TRAVEL_DOC_GROUP_EC = "EC";//Emergency Contact
	public static final String TRAVEL_DOC_GROUP_DA = "DA";//Destination Address
	public static final String TRAVEL_DOC_GROUP_CR = "CR";//Country Of Residence
	public static final String TRAVEL_DOC_GROUP_PT = "PT";//Primary Travel Document
	public static final String TRAVEL_DOC_GROUP_ST = "ST";//Secondary Travel Document
	public static final String TRAVEL_DOC_GROUP_TS = "TS";//KTN in OLCI
	public static final String TRAVEL_DOC_GROUP_KN = "KN";//KTN
	public static final String TRAVEL_DOC_GROUP_RN = "RN";//Redress Number
	public static final String MANDATORY_TRUE = "Y";
	public static final String MANDATORY_FALSE = "N";

	/** SSR SK rule mapping */
	public static final String SEAT_SELECTION_INHIBIT="E";
	public static final String SEAT_SELECTION_VALID="A";
	public static final String SPECIAL_SEAT_SELECTION_EXITROW="X";
	public static final String UPGRADE_BID_INHIBIT="E";
	
	
	/** field value in table TB_PORT_FLIGHT */
	public static final String SEGMENT_ACTION_INHIBIT = "I";
	public static final String SEGMENT_ACTION_EXCLUDE = "E";
	public static final String SEGMENT_ACTION_DISPLAY = "D";
	public static final String SEGMENT_ACTION_ALLOW = "A";
	public static final String TB_PORT_FLIGHT_WILD_CARD_FOR_AIRLINE_CODE = "**";
	public static final String TB_PORT_FLIGHT_WILD_CARD_FOR_ORIGIN = "***";
	public static final String TB_PORT_FLIGHT_WILD_CARD_FOR_DESTINATION = "***";
	
	/**
	 * table:tb_1a_error_handle, filed:1a_error_handle.Error handle stop identifier: S = STOP
	 */
	public static final String ERROR_HANDLE_IDENTIFIER_STOP = "S";
	
	/**
	 * table:tb_1a_error_handle, filed:1a_error_handle.Error handle stop identifier: L = Light Box
	 */
	public static final String ERROR_HANDLE_IDENTIFIER_LIGHTBOX = "L";
	
	/**
	 *table:tb_1a_error_handle, filed:1a_error_handle. Error handle stop identifier: B = BYPASS
	 */
	public static final String ERROR_HANDLE_IDENTIFIER_BYPASS = "B";
	/**
	 * table:tb_1a_error_handle, filed:1a_error_handle.Error handle stop identifier: D = DISPLAY
	 */
	public static final String ERROR_HANDLE_IDENTIFIER_DISPLAY = "D";
	
	/** value for last_update_source in DB */
	public static final String UPDATE_SOURCE = "SYSTEM";
	
	/** seat rule */
	public static final String ASR_FOC_YES="Y";
	public static final String COMPANION_ASR_FOC_YES="Y";
	public static final String SELECTED_ASR_FOC_YES="Y";
	public static final String ELIGIBILITY_SEAT_SELECT_VALID="A";
	public static final String IS_LOW_RBD="Y";
	
	/** table tb_status_management, field = value */
	public static final String TB_STATUS_MANAGEMENT_VALUE_WILDCARD="*";
	public static final String TB_STATUS_MANAGEMENT_TYPE_SSR="SSR";


	/**table tb_reminder_code field action **/
	public static final String BOOKINGWAIVE_REMINDER_FUNCTIONNAME = "bookingwaive";

	public static final String REMINDER_SPECIALSERVICE_KEY = "specialservice";

	/** table tb_officeid_mapping type **/
	public static final String TB_OFFICE_ID_MAPPING_TYPE_IBE = "IBE";
	public static final String TB_OFFICE_ID_MAPPING_TYPE_NDC = "NDC";
	public static final String TB_OFFICE_ID_MAPPING_TYPE_CBWL = "CBWL";
	public static final String TB_OFFICE_ID_MAPPING_TYPE_GCC = "GCC";
	public static final String TB_OFFICE_ID_MAPPING_TYPE_ARO = "ARO";
	public static final String TB_OFFICE_ID_MAPPING_TYPE_ATO = "ATO";
	public static final String TB_OFFICE_ID_MAPPING_TYPE_CTO = "CTO";
	public static final String TB_OFFICE_ID_MAPPING_TYPE_WORLDBE = "WORLDBE";
	
	/** table tb_flight_haul */
	public static final String TB_FLIGHT_HAUL_OPT_CX_EXIST = "Y";
	public static final String TB_FLIGHT_HAUL_OPT_CX_NON_EXIST = "N";
	public static final String TB_FLIGHT_HAUL_OPT_KA_EXIST = "Y";
	public static final String TB_FLIGHT_HAUL_OPT_KA_NON_EXIST = "N";
	
	/** table tb_official_receipt_download_history*/
	public static final String TB_OFFICIAL_RECEIPT_DOWNLOAD_REFERENCE_PREFIX = "MBOR";
	
	/** table tb_open_close_time*/
	public static final String TB_TB_OPEN_CLOSE_TIME_PAX_TYPE_C = "C";
	/** table tb_open_close_time*/
	public static final String TB_TB_OPEN_CLOSE_TIME_PAX_TYPE_SB = "SB";
	/** table tb_open_close_time*/
	public static final String TB_TB_OPEN_CLOSE_TIME_PAX_TYPE_SS = "SS";
	
	/** tb_regulatory_country_mapping*/
	public static final String TB_REGULATORY_COUNTRY_MAPPING_COUNTRYCODE_WILDCARD = "*";

	/** tb_redemption_asr_check*/
	public static final String TB_REDEMPTION_ASR_IS_FOC = "Y";
	public static final String TB_REDEMPTION_ASR_IS_NOT_FOC = "N";
}

