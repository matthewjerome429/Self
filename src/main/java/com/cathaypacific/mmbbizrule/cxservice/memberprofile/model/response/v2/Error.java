package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2;

public class Error {

	private String code;
	
	private String message;
	
	private String[] parameters;
	
	private ClsError clsError;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getParameters() {
		return parameters;
	}

	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}

	public ClsError getClsError() {
		return clsError;
	}

	public void setClsError(ClsError clsError) {
		this.clsError = clsError;
	}
	
}
