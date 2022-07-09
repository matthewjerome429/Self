package com.cathaypacific.mbcommon.constants;

public class MMBConstants {

	public static final String APP_CODE = "MMB";
	
	
	public static final String APP_CODE_OLCI = "OLCI";
	
	/**header part*/
	public static final String HEADER_KEY_MMB_TOKEN_ID = "MMB-Token";
	public static final String HEADER_KEY_ACCEPT_LANGUAGE = "Accept-Language";
	public static final String HEADER_KEY_ACCESS_CHANNEL = "Access-Channel";
	public static final String HEADER_KEY_APP_CODE= "App-Code";
	public static final String Locale = "Locale";
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String CONTENT_DISPOSITION_VALUE_PREFIX = "attachment; filename=";
	public static final String DEFAULT_ACCEPT_LANGUAGE = "en_HK";
	public static final String DEFAULT_APP_CODE = "MMB";
	
	/** header http part*/
	public static final String HTTP_CONTENT_TYPE_PKPASS = "application/vnd.apple.pkpass";
	
	public static final String HTTP_HEADER_KEY_CONTENT_DISPOSITION = "Content-Disposition";
	
	public static final String HTTP_HEADER_VALUE_CONTENT_DISPOSITION_PKPASS = "attachment; filename=BoardingPass.pkpass";
	
	/** flight number standard length */
	public static final int FLIGHT_NUMEBR_STANDARD_LENGTH = 4;
	
	/** special country number */
	public static final String SPECIAL_COUNTRY_NUMBER = "1";
	/** special country number with + */
	public static final String SPECIAL_COUNTRY_NUMBER_WITH_PREFIX = "+1";
	/** use this country while country number is 1 and no area code is found */
	public static final String COUNTRY_CODE_FOR_NO_AREA_CODE_MAPPED = "US";
    
	/** AM member */
	public static final String AM_MEMBER = "AM";
	/** MPO member */
	public static final String MPO_MEMBER = "MPO";
	/** RU member */
	public static final String RU_MEMBER = "RU";
	
	/** Company ID CX */
	public static final String COMPANY_CX = "CX";
	
	/** Company ID KA */
	public static final String COMPANY_KA = "KA";
	
	/** Log mask sign */
	public static final char LOG_MASK_SIGN = '*';
	
    private MMBConstants(){
    	
    }
}
