package com.cathaypacific.mbcommon.constants;

public class MDCConstants {
	
	public static final String REQUEST_TRACE_ID_MDC_KEY = "X-B3-TraceId"; // from spring cloud sleuth
	
	public static final String REQUEST_SESSION_ID_MDC_KEY = "req.sessionId";
	public static final String REQUEST_X_FORWARDED_FOR_MDC_KEY = "req.xForwardedFor";
	public static final String REQUEST_CLIENT_IP_MDC_KEY = "req.clientIp";
	public static final String REQUEST_USER_AGENT_MDC_KEY = "req.userAgent";
	public static final String REQUEST_USER_SESSION_ID = "req.userSessionId";
	public static final String REQUEST_ACCESS_CHANNEL = "req.accessChannel";
	public static final String REQUEST_REFERER_KEY = "req.referer";
	
	public static final String CLASS_NAME_MDC_KEY = "app.className";
	public static final String METHOD_NAME_MDC_KEY = "app.methodName";
	
	public static final String MESSAGE_TYPE_KEY = "app.messageType";
	
	public static final String OPERATION_KEY = "app.operation";
	
	public static final String LOGIN_MDC_KEY = "app.loginKey";
	public static final String MMB_TOKEN_MDC_KEY = "app.mmbToken";
	public static final String APP_CODE_MDC_KEY = "app.appCode";
	public static final String ACCESS_CHANNEL_MDC_KEY = "app.accessChannel";
	public static final String ACCEPT_LANGUAGE_MDC_KEY = "app.acceptLanguage";
	
	public static final String RESPONSE_TIME_MDC_KEY = "app.responseTime.ms";
	public static final String DATA_SIZE_MDC_KEY = "app.dataSize.kb";
	
	public static final String REQUEST_RESPONSE_BODY_MDC_KEY = "app.reqOrResp";
	public static final String REQUEST_RESPONSE_HEADER_MDC_KEY = "app.reqOrRespHeader";
	
	public static final String LOG_ENTRY_TYPE_MDC_KEY = "app.logEntryType";
	
	public static final String ERRORCODE_MDC_KEY = "app.errorCode";
	
	public static final String INCOMING_CHANNEL = "incomingChannel";
	public static final String INCOMING_MICROSERVICE = "incomingMicroservice";

	public static final String HTTPBODY_REQUEST_RESPONSEMDC_KEY = "req.inOrOut";

	public static final String REQUEST_SERVICEURI_MDC_KEY = "req.serviceUri";

	public static final String REQUEST_HTTPMETHOD_MDC_KEY = "req.httpMethod";

	public static final String REQUEST_QUERYSTRING_MDC_KEY = "req.queryString";
	
	public static final String LOG_TYPE_MDC_KEY = "@type";
	
}
