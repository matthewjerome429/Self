package com.cathaypacific.mmbbizrule.cxservice.memberaward.model.response;

import java.util.List;

public class MemberAwardError {
	private String code;
	
	private String message;

	private List<String> parameters;

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

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public ClsError getClsError() {
		return clsError;
	}

	public void setClsError(ClsError clsError) {
		this.clsError = clsError;
	}

}
