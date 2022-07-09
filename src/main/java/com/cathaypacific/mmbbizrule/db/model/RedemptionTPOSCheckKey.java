package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class RedemptionTPOSCheckKey  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
	
	@NotNull
	@Column(name = "type", length = 10)
	private String type;
	
	@NotNull
	@Column(name = "value", length = 10)
	private String value;
	
	public RedemptionTPOSCheckKey() {
	}
	
	public RedemptionTPOSCheckKey(String appCode, String type, String value) {
		this.appCode = appCode;
		this.type = type;
		this.value = value;
	}

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
