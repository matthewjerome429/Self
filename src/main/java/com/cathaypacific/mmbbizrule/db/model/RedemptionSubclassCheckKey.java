package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class RedemptionSubclassCheckKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
	
	@NotNull
	@Column(name = "operate_company", length = 10)
	private String operateCompany;
	
	@NotNull
	@Column(name = "subclass", length = 10)
	private String subclass;

	
	public RedemptionSubclassCheckKey() {
	}
	public RedemptionSubclassCheckKey(String appCode, String operateCompany, String subclass) {
		this.appCode = appCode;
		this.operateCompany = operateCompany;
		this.subclass = subclass;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getOperateCompany() {
		return operateCompany;
	}

	public void setOperateCompany(String operateCompany) {
		this.operateCompany = operateCompany;
	}

	public String getSubclass() {
		return subclass;
	}

	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}
	
	
}
