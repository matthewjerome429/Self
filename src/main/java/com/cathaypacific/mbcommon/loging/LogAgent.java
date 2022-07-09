package com.cathaypacific.mbcommon.loging;


import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import net.logstash.logback.argument.StructuredArguments;

public class LogAgent {
	
	public static final String MESSAGE_TYPE_INT = "INT"; 		// interface
	
	public static final String MESSAGE_TYPE_STATS = "stats";
	
	private Logger logger;
	
	private LogAgent(Class<?> clazz){

		this.logger = LoggerFactory.getLogger(clazz);
	}
	
	public static LogAgent getLogAgent(Class<?> clazz){
		return new LogAgent(clazz);
	}
	
	/* 
	 * Designates fine-grained informational events that are most useful to debug an application.
	 */
	public void debug(String message){
		logger.debug(message);
	}
	
	public void debug(String message, Throwable exceptionObject){
		logger.debug(message, exceptionObject);
	}
	
	/*
	 * Designates informational messages that highlight the progress of the application at coarse-grained level.
	 */
	public void info(String message){
		logger.info(message);
	}
	
	public void info(String message, Boolean stats){
		if(BooleanUtils.isTrue(stats)) {
			logger.info(message,StructuredArguments.kv(MDCConstants.LOG_TYPE_MDC_KEY, MESSAGE_TYPE_STATS));
		} else {
			logger.info(message);			
		}
	}
	
	/* for each interface request only */
	public void info(String methodName, String message){
		logger.info(message,StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName));
	}
	
	public void info(String methodName, String message, String operation){
		logger.info(message, StructuredArguments.kv(MDCConstants.OPERATION_KEY, operation),
				StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName));
	}
	
	// log api enter
	public void info(String methodName, String logMsg, String httpMethod, String queryString) {
		logger.info(logMsg, StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName),
				StructuredArguments.kv(MDCConstants.REQUEST_HTTPMETHOD_MDC_KEY, httpMethod),
				StructuredArguments.kv(MDCConstants.REQUEST_QUERYSTRING_MDC_KEY, queryString));
	}
		
	// log api requestBody
	public void httpReqInfo(String methodName, String logMsg, String requestBody) {
		logger.info(logMsg, StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName),
				StructuredArguments.kv(MDCConstants.HTTPBODY_REQUEST_RESPONSEMDC_KEY, requestBody));
	}
	
	/* for each successful login only */
	public void info(String methodName, String message, String operation, LoginInfo loginInfo, String mmbToken){
		
		String loginMDCValue = "";
		loginMDCValue += (loginInfo.getLoginType() != null) ? loginInfo.getLoginType() + "|" : "";
		loginMDCValue += (loginInfo.getMemberId() != null) ? loginInfo.getMemberId() + "|" : "";
		loginMDCValue += (loginInfo.getLoginEticket() != null) ? loginInfo.getLoginEticket() + "|" : "";
		loginMDCValue += (loginInfo.getLoginFamilyName() != null) ? loginInfo.getLoginFamilyName() + "|" : "";
		loginMDCValue += (loginInfo.getLoginGivenName() != null) ? loginInfo.getLoginGivenName() + "|" : "";
		loginMDCValue += (loginInfo.getLoginRloc() != null) ? loginInfo.getLoginRloc() + "|" : "";
		
		logger.info(message, StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName),
				StructuredArguments.kv(MDCConstants.OPERATION_KEY, operation), StructuredArguments.kv(MDCConstants.LOGIN_MDC_KEY, loginMDCValue));
	}
	
	public void info(String methodName, String message, String operation, LoginInfo loginInfo, String mmbToken, boolean stats){
		
		String loginMDCValue = "";
		loginMDCValue += (loginInfo.getLoginType() != null) ? loginInfo.getLoginType() + "|" : "";
		loginMDCValue += (loginInfo.getMemberId() != null) ? loginInfo.getMemberId() + "|" : "";
		loginMDCValue += (loginInfo.getLoginEticket() != null) ? loginInfo.getLoginEticket() + "|" : "";
		loginMDCValue += (loginInfo.getLoginFamilyName() != null) ? loginInfo.getLoginFamilyName() + "|" : "";
		loginMDCValue += (loginInfo.getLoginGivenName() != null) ? loginInfo.getLoginGivenName() + "|" : "";
		loginMDCValue += (loginInfo.getLoginRloc() != null) ? loginInfo.getLoginRloc() + "|" : "";
		
		if(stats) {
			logger.info(message, StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName),
					StructuredArguments.kv(MDCConstants.OPERATION_KEY, operation), 
					StructuredArguments.kv(MDCConstants.LOGIN_MDC_KEY, loginMDCValue),
					StructuredArguments.kv(MDCConstants.LOG_TYPE_MDC_KEY, MESSAGE_TYPE_STATS));
		} else {
			logger.info(message, StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName),
					StructuredArguments.kv(MDCConstants.OPERATION_KEY, operation),
					StructuredArguments.kv(MDCConstants.LOGIN_MDC_KEY, loginMDCValue));						
		}
	}
	
	// log the interface message request and response
	public void info(String methodName, String requestMsg, int requestMsgSize, String message) {
		logger.info(message, StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName),
				StructuredArguments.kv(MDCConstants.REQUEST_RESPONSE_BODY_MDC_KEY, requestMsg),
				StructuredArguments.kv(MDCConstants.DATA_SIZE_MDC_KEY, (requestMsgSize / 1024.0)),
				StructuredArguments.kv(MDCConstants.MESSAGE_TYPE_KEY, MESSAGE_TYPE_INT));
	}
	
	public void info(String methodName, String requestMsg, String header, int requestMsgSize, String message) {
		logger.info(message, StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName), 
				StructuredArguments.kv(MDCConstants.REQUEST_RESPONSE_BODY_MDC_KEY, requestMsg),
				StructuredArguments.kv(MDCConstants.REQUEST_RESPONSE_HEADER_MDC_KEY, header),
				StructuredArguments.kv(MDCConstants.DATA_SIZE_MDC_KEY, (requestMsgSize / 1024.0)),
				StructuredArguments.kv(MDCConstants.MESSAGE_TYPE_KEY, MESSAGE_TYPE_INT));
	}
	
	// log the performance
	public void info(String methodName, long startTime, String logMsg) {
		String msg = "[TimeDetails][" + logMsg + "][Time: " + DurationFormatUtils.formatDuration(startTime, "HH:mm:ss,SSS") + "]";
		logger.info(msg, StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName),
				StructuredArguments.kv(MDCConstants.RESPONSE_TIME_MDC_KEY, startTime));
	}
		
	// log the performance with response
	public void info(String methodName, long startTime, String logMsg, String response) {
		String msg = "[TimeDetails][" + logMsg + "][Time: " + DurationFormatUtils.formatDuration(startTime, "HH:mm:ss,SSS") + "]";
		logger.info(msg, StructuredArguments.kv(MDCConstants.METHOD_NAME_MDC_KEY, methodName),
				StructuredArguments.kv(MDCConstants.RESPONSE_TIME_MDC_KEY, startTime),
				StructuredArguments.kv(MDCConstants.HTTPBODY_REQUEST_RESPONSEMDC_KEY, response));
	}
	
	/*
	 * Designates potentially harmful situations.
	 */	
	public void warn(String message, String operation, String errorCode){

		logger.warn(message, StructuredArguments.kv(MDCConstants.ERRORCODE_MDC_KEY, errorCode),
				StructuredArguments.kv(MDCConstants.OPERATION_KEY, operation));
	}
	
	public void warn(String message, String operation, Exception e){
		logger.warn(message, e, StructuredArguments.kv(MDCConstants.OPERATION_KEY, operation));
	}
	
	public void warn(String message, Exception e){
		logger.warn(message, e);
	}
	
	public void warn(String message, String errorCode){
		logger.warn(message, StructuredArguments.kv(MDCConstants.ERRORCODE_MDC_KEY, errorCode));
	}
	
	public void warn(String message){
		logger.warn(message);
	}
	
	/*
	 * Designates error events that might still allow the application to continue running.
	 */
	public void error(String message, String errorCode, Exception e){
		logger.error(message, StructuredArguments.kv(MDCConstants.ERRORCODE_MDC_KEY, errorCode), e);
	}
	
	public void error(String message, String errorCode){
		logger.error(message, StructuredArguments.kv(MDCConstants.ERRORCODE_MDC_KEY, errorCode));
	}
	
	public void error(String message, Exception e){
		logger.error(message, e);
	}
	
	public void error(String message){
		logger.error(message);
	}
	
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
}
