package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;

public class TbOfficeIdMappingKey implements Serializable {
	
	private static final long serialVersionUID = 3285551421093973778L;

	@Column(name = "app_code")
	private String appCode;

	@Column(name = "type")
	private String type;
	
	@Column(name = "value")
	private String value;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
