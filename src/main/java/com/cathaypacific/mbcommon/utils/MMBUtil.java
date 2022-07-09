package com.cathaypacific.mbcommon.utils;

import java.util.Objects;

import org.slf4j.MDC;

import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.model.login.LoginInfo;

public class MMBUtil {

	public static String APPCODE_MMB = "MMB";
	public static String APPCODE_MMB_OLCI_LOGIN = "MMB_OLCI_LOGIN";	 // this app-code will be used when login by MMB Inflow-OLCI. Only for login API.
	
	private MMBUtil(){
		//do nothing
	}
	/**
	 * Get mmb token form MDC, MDC should has MMB token always in current design
	 * @return
	 */
	public static String getCurrentMMBToken(){
		return MDC.get(MDCConstants.MMB_TOKEN_MDC_KEY);
	}
	
	/**
	 * Get Accept Language form MDC, MDC should has MMB token always in current design
	 * @return
	 */
	public static String getCurrentAcceptLanguage(){
		return MDC.get(MDCConstants.ACCEPT_LANGUAGE_MDC_KEY);
	}
	
	/**
	 * Get  Current appCode form MDC, MDC should has MMB token always in current design
	 * @return
	 */
	public static String getCurrentAppCode(){
		return MDC.get(MDCConstants.APP_CODE_MDC_KEY);
	}
	
	/**
	 * DON'T USE THIS METHOD IF NOT REALLY NESCESSARY
	 * Set a custom app code
	 * Use case: a business rule is required to check for a specific UX flow and only applicable for MMB app
	 * @param appCode
	 */
	public static void setAsMMBAppCode(String appCode) {
		MDC.put(MDCConstants.APP_CODE_MDC_KEY, APPCODE_MMB);
	}
	
	/**
	 * Get Current Access Channel form MDC, please note it is may be empty
	 * @return
	 */
	public static String getCurrentAccessChannel(){
		return MDC.get(MDCConstants.ACCESS_CHANNEL_MDC_KEY);
	}
	
	
	/**
	 * Check the login member is RU
	 * @return
	 */
	public static boolean isRuLogin(LoginInfo loginInfo){
		return Objects.equals(MMBConstants.RU_MEMBER, loginInfo.getUserType());
	}
	
	/**
	 * Check the login member is AM
	 * @return
	 */
	public static boolean isAmLogin(LoginInfo loginInfo){
		return Objects.equals(MMBConstants.AM_MEMBER, loginInfo.getUserType());
	}
	
	/**
	 * Check the login member is MPO
	 * @return
	 */
	public static boolean isMpoLogin(LoginInfo loginInfo){
		return Objects.equals(MMBConstants.MPO_MEMBER, loginInfo.getUserType());
	}
}
