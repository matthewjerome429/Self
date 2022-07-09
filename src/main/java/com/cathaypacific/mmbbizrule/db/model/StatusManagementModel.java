package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tb_status_management")
@IdClass(StatusManagementKey.class)
public class StatusManagementModel {
	
	@Id
	@NotNull
    @Column(name="app_code")
	private String appCode;
	
	@Id
	@NotNull
    @Column(name="type")
	private String type;
	
	@Id
	@NotNull
    @Column(name="value")
	private String value;
	
	@Id
	@NotNull
	@Column(name="status_code")
	private String statusCode;
	
	@NotNull
	@Column(name="mmb_status")
	private String mmbStatus;
	
	@Column(name="description")
	private String description;
	
	@NotNull
    @Column(name = "last_update_source")
    private String lastUpdateSource;

    @NotNull
    @Column(name = "last_update_timestamp")
    private Date lastUpdateTimeStamp;

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

	public String getMmbStatus() {
		return mmbStatus;
	}

	public void setMmbStatus(String mmbStatus) {
		this.mmbStatus = mmbStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
