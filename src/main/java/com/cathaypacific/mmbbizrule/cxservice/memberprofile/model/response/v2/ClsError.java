package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2;

public class ClsError {
	
	private int errorStatusCode;
	
	private String errorMessage;
	
	private String errorMessageTextToInsert;
	
	private String errorStatusRollbackInc;
	
	private String errorStatusLocation;
	
	private String messageDisplayIndicator;
	
	private String errorSeverity;

	public int getErrorStatusCode() {
		return errorStatusCode;
	}

	public void setErrorStatusCode(int errorStatusCode) {
		this.errorStatusCode = errorStatusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessageTextToInsert() {
		return errorMessageTextToInsert;
	}

	public void setErrorMessageTextToInsert(String errorMessageTextToInsert) {
		this.errorMessageTextToInsert = errorMessageTextToInsert;
	}

	public String getErrorStatusRollbackInc() {
		return errorStatusRollbackInc;
	}

	public void setErrorStatusRollbackInc(String errorStatusRollbackInc) {
		this.errorStatusRollbackInc = errorStatusRollbackInc;
	}

	public String getErrorStatusLocation() {
		return errorStatusLocation;
	}

	public void setErrorStatusLocation(String errorStatusLocation) {
		this.errorStatusLocation = errorStatusLocation;
	}

	public String getMessageDisplayIndicator() {
		return messageDisplayIndicator;
	}

	public void setMessageDisplayIndicator(String messageDisplayIndicator) {
		this.messageDisplayIndicator = messageDisplayIndicator;
	}

	public String getErrorSeverity() {
		return errorSeverity;
	}

	public void setErrorSeverity(String errorSeverity) {
		this.errorSeverity = errorSeverity;
	}
	
}
