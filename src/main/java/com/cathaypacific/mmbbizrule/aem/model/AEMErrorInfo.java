package com.cathaypacific.mmbbizrule.aem.model;

import java.io.Serializable;

public class AEMErrorInfo implements Serializable {
	
	private static final long serialVersionUID = -6988565196887103186L;
	private String code;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
