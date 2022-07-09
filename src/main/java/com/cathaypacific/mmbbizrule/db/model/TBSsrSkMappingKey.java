package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class TBSsrSkMappingKey implements Serializable{

	private static final long serialVersionUID = -1098867454722342202L;
	
	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;
	@Id
	@NotNull
	@Column(name = "SSR_SK_CODE", length = 4)
	private String ssrSkCode;
	
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String getSsrSkCode() {
		return ssrSkCode;
	}
	public void setSsrSkCode(String ssrSkCode) {
		this.ssrSkCode = ssrSkCode;
	}
}
