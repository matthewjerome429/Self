package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class StatusManagementKey implements Serializable {
	
	private static final long serialVersionUID = 990862242469581157L;

	@NotNull
    @Column(name="app_code")
	private String appCode;
	
	@NotNull
    @Column(name="type")
	private String type;
	
	@NotNull
    @Column(name="value")
	private String value;
	
	@NotNull
	@Column(name="status_code")
	private String statusCode;

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

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
}
