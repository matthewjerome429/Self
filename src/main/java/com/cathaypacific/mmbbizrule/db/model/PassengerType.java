package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tb_passenger_type")
@IdClass(PassengerTypeKey.class)
public class PassengerType {
	
    @Id
    @NotNull
    @Column(name = "app_code")
    private String appCode;
    
    @Id
    @NotNull
    @Column(name = "pnr_passenger_type")
    private String pnrPassengerType;
    
    @NotNull
    @Column(name = "passenger_type")
    private String paxType;
    
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

	public String getPnrPassengerType() {
		return pnrPassengerType;
	}

	public void setPnrPassengerType(String pnrPassengerType) {
		this.pnrPassengerType = pnrPassengerType;
	}

	public String getPaxType() {
		return paxType;
	}

	public void setPaxType(String paxType) {
		this.paxType = paxType;
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
