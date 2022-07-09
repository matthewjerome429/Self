package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

public class ErrorInfo {

	private String errorCode;
	
	private String type;
	
	private String message;

	public ErrorInfo(ErrorCodeEnum errorCodeEnum) {
		this.errorCode = errorCodeEnum.getCode();
		this.type = errorCodeEnum.getType().getType();
		this.message = errorCodeEnum.getMessage();
	}

	public ErrorInfo() {
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
