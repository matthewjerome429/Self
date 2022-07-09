package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tb_additional_operator_info")
@IdClass(AdditionalOperatorInfoKey.class)
public class AdditionalOperatorInfoModel {

    @Id
    @NotNull
    @Column(name = "app_code")
    private String appCode;
    
    @Id
    @NotNull
    @Column(name = "operator_name")
    private String operatorName;
    
    @Column(name = "operator_code")
    private String operatorCode;
    
    @Column(name = "airline_code")
    private String airlineCode;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "last_update_source")
    private String lastUpdateSource;

    @Column(name = "last_update_timestamp")
    private Date lastUpdateTimeStamp;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
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
