package com.cathaypacific.mmbbizrule.constant;

public class MLCConstants {

	/**
	 * Constant Variable Used to Read MLCsession Xml.
	 */
	public static final String SESSION_ID = "sessionID";
	/**
	 * Constant Variable Used to Read MLCsession Xml.
	 */
	public static final String MEMBER_ID = "memID";
	/**
	 * Constant Variable Used to Read MLCsession Xml.
	 */
	public static final String USER_TYPE = "userType";
	/**
	 * Constant Variable Used to Read MLCsession Xml.
	 */
	public static final String PRE_LINK = "preLink";
	/**
	 * Constant variable Used to Read MLCsession Xml.
	 */
	public static final String STATUS_CODE = "statusCode";
	/**
	 * Constant Variable Used to Read MLCsession Xml.
	 */
	public static final String IS_JUST_LOGIN = "isJustLogin";
	/**
	 * Session Not Found errrorCode.
	 */
	public static final String SESSION_NOT_FOUND = "ERR_COMM_004";
	/**
	 * Invalid token errorcode.
	 */
	public static final String INVALID_TOKEN = "ERR_LOGIN_002";
	/**
	 * Expired token errorcode.
	 */
	public static final String EXPIRED_TOKEN = "ERR_LOGIN_005";

	/** The Constant INVALID_REQUEST. */
	public static final String INVALID_REQUEST = "ERR_MPO_084";
	/**
	 * Invalid token status code.
	 */
	public static final int INVALID_TOKEN_STATUS_CODE = 1002;
	/**
	 * UTF-8 encoding variable.
	 */
	public static final String ENCODING_STANDARD = "UTF-8";
	/**
	 * To append action to MLC url.
	 */
	public static final String APPEND_ACTION = "?action=";
	/**
	 * To append action to Login URL Error.
	 */
	public static final String APPEND_ERRORCODE = "?errorCode=";
	/**
	 * To append target url to MLC url.
	 */
	public static final String APPEND_TARGETURL = "&targetURL=";
	/**
	 * To append login url to MLC url.
	 */
	public static final String APPEND_LOGINURL = "&loginURL=";
	/**
	 * To append application code to MLC url.
	 */
	public static final String APPEND_APPCODE = "&appCode=";
	/**
	 * To append member id to MLC url.
	 */
	public static final String APPEND_MEMBERID = "&memID=";
	/**
	 * To append member pin to MLC url.
	 */
	public static final String APPEND_MEMBERPIN = "&memPIN=";
	/**
	 * To append member token to MLC url.
	 */
	public static final String APPEND_TOKEN = "&token=";
	/**
	 * To append member token to MLC url.
	 */
	public static final String URL_APPEND_TOKEN = "?token=";
	/**
	 * To append member token to MLC url.
	 */
	public static final String APPEND_STATUS = "?status=";
	/**
	 * MLC authenticate url defined in property file.
	 */
	public static final String MLC_AUTHENTICATE_URL = "MLC_AUTHENTICATE_URL";
	/**
	 * MLC authenticate action defined in property file.
	 */
	public static final String MLC_AUTHENTICATE_ACTION = "MLC_AUTHENTICATE_ACTION";

	/**
	 * MLC logout url defined in property file.
	 */
	public static final String MLC_LOGOUT_URL = "MLC_LOGOUT_URL";
	/**
	 * Variable for ZERO.
	 */
	public static final int ZERO = 0;
	/**
	 * Variable for ZERO.
	 */
	public static final int STATUS_CODE_VALID_TOKEN = 0;
	/**
	 * Variable for ZERO.
	 */
	public static final int STATUS_CODE_EMPTY_TOKEN = 1001;
	
	/**
	 * Variable for ZERO.
	 */
	public static final int STATUS_CODE_INVALID_TOKEN = 1002;
	
	/**
	 * To append redirect URL.
	 */
	public static final String REDIRECT_URL = "?redirectUrl=";
	
	/**
	 * To append fallback URL.
	 */
	public static final String FALLBACK_URL = "&fallbackUrl=";
	
	/**
     * Private Constructor.
     */
    private MLCConstants() {
    	
    }
}
