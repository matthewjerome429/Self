package com.cathaypacific.mbcommon.constants;

public class OneAErrorConstants {
	/**
	 * MMB action value in table TB_1A_ERROR_HANDLE: 00 = No specific action
	 */
	public static final String MMB_ACTION_NO_SPECIFIC = "00";
	/**
	 * App code in error code: 2 = MMB
	 */
	public static final String APPCODE_IN_ERRORCODE_MMB = "2";
	
	/**
	 * Source code in error code: 1 = 1A web service
	 */
	public static final String SOURCECODE_IN_ERRORCODE = "1";
	
	/**
	 * The prefix in error for 1A, e.g.: 1A errorCode=10447 ---> Error in app is: Exxx10447
	 */
	public static final String ONEAERROR_PREFIX_IN_ERRORCODE = "E";
	
	/**
	 * Error handle stop identifier: S = STOP
	 */
	public static final String ERROR_HANDLE_IDENTIFIER_STOP = "S";
	
	private OneAErrorConstants() {
		// Empty constructor.
	}
}
