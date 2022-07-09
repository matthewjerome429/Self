package com.cathaypacific.mbcommon.exception;

public class SoapFaultException extends BusinessBaseException {

	private static final long serialVersionUID = 1396904205590016255L;

	private final String errorCode;

	private final String errorMessage;

	private final String errorType;
	
	private final String interfaceCode;

	public SoapFaultException(String errorCode, String errorType, String errorMessage,String interfaceCode) {
		super(errorCode + "|" + errorType + "|" + errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorType = errorType;
		this.interfaceCode = interfaceCode;
	}
	
	public SoapFaultException(String errorCode, String errorType, String errorMessage,String interfaceCode,Throwable cause ) {
		super(errorCode + "|" + errorType + "|" + errorMessage,cause);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorType = errorType;
		this.interfaceCode = interfaceCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public String getInterfaceCode() {
		return interfaceCode;
	}

}
