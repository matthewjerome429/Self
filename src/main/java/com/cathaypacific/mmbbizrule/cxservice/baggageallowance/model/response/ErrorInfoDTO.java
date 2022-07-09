package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response;

import java.io.Serializable;

public class ErrorInfoDTO implements Serializable {

	private static final long serialVersionUID = 6113546187963296391L;

	private String source;

	private String code;

	private String type;

	private String message;

	private String details;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
