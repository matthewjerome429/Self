package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class ErrorInfo implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -7269633528859375039L;

	private String errorCode;
	/**
	 * error type:S = Stop; D = Display; B = Bypass; L = Display (lightbox)
	 */
	private String type;

	private String fieldName;

	public ErrorInfo() {
		// Default constructor
	}

	public ErrorInfo(String errorCode, String errorType) {
		this.errorCode = errorCode;
		this.type = errorType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
