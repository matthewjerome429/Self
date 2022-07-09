package com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.response;

import java.io.Serializable;
import java.util.List;

public class ErrorDTO implements Serializable {
	
	private static final long serialVersionUID = 1307086914378395906L;

	private String code;
	
	private List<String> parameters;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	
}
