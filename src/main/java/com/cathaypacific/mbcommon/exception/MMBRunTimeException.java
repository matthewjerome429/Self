package com.cathaypacific.mbcommon.exception;

import org.springframework.http.HttpStatus;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class MMBRunTimeException extends RuntimeException {

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 2803407019940110166L;

	private final ErrorInfo errorInfo;
	
	private final HttpStatus httpStatus;


	public MMBRunTimeException(String message,ErrorInfo errorInfo) {
		super(message);
		this.errorInfo = errorInfo;
		this.httpStatus = null;
	}

	public MMBRunTimeException(String message, ErrorInfo errorInfo, HttpStatus httpStatus ){
		super(message);
		this.errorInfo = errorInfo;
		this.httpStatus = httpStatus;
	}
	public MMBRunTimeException(String message,ErrorInfo errorInfo,Throwable cause) {
		super(message,cause);
		this.errorInfo = errorInfo;
		this.httpStatus = null;
	}

	public MMBRunTimeException(String message, ErrorInfo errorInfo, HttpStatus httpStatus,Throwable cause ){
		super(message,cause);
		this.errorInfo = errorInfo;
		this.httpStatus = httpStatus;
	}

	public ErrorInfo getErrorInfo() {
		return errorInfo;
	}


	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}