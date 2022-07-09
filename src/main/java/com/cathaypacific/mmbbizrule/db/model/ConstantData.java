package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_CONSTANT_DATA")
@IdClass(ConstantDataKey.class)
public class ConstantData{
	
	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;
	
	@Id
	@NotNull
	@Column(name = "TYPE", length = 10)
	private String type;
	
	@Id
	@NotNull
	@Column(name = "VALUE", length = 500)
	private String value;
	
	@NotNull
	@Column(name = "LAST_UPDATE_SOURCE", length = 8)
	private String lastUpdateSource;
	
	@NotNull
	@Column(name = "LAST_UPDATE_TIMESTAMP")
	private Date lastUpdateTimeStamp;

	public ConstantData() {
		// Empty constructor.
	}
	
	public ConstantData(String appCode, String type, String value, String lastUpdateSource, Date lastUpdateTimeStamp) {
		this.appCode = appCode;
		this.type = type;
		this.value = value;
		this.lastUpdateSource = lastUpdateSource;
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
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
