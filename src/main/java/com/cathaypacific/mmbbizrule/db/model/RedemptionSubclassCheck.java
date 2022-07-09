package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_REDEMPTION_SUBCLASS_CHECK")
@IdClass(RedemptionSubclassCheckKey.class)
public class RedemptionSubclassCheck {
	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;
	
	@Id
	@NotNull
	@Column(name = "OPERATE_COMPANY", length = 10)
	private String operateCompany;
	
	@Id
	@NotNull
	@Column(name = "SUBCLASS", length = 10)
	private String subclass;
	
	@NotNull
	@Column(name = "LAST_UPDATE_SOURCE", length = 8)
	private String lastUpdateSource;
	
	@NotNull
	@Column(name = "LAST_UPDATE_TIMESTAMP")
	private Date lastUpdateTimeStamp;

	public RedemptionSubclassCheck() {
	}
	public RedemptionSubclassCheck(String appCode, String operateCompany, String subclass, String lastUpdateSource, Date lastUpdateTimeStamp) {
		this.appCode = appCode;
		this.operateCompany = operateCompany;
		this.subclass = subclass;
		this.lastUpdateSource = lastUpdateSource;
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
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

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Date getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

	public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}
	


}
