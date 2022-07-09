package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_POWER_VOLTAGE_FREQUENCY")
@IdClass(PowerVoltageFrequencyKey.class)
public class PowerVoltageFrequency {
	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;

	@Id
	@NotNull
	@Column(name = "COUNTRY_CODE", length = 5)
	private String countryCode;

	@NotNull
	@Column(name = "POWER_VOLTAGE", length = 8)
	private String powerVoltage;
	
	@NotNull
	@Column(name = "POWER_FREQUENCY", length = 10)
	private String powerFrequency;

	@NotNull
	@Column(name = "LAST_UPDATE_SOURCE", length = 8)
	private String lastUpdateSource;

	@NotNull
	@Column(name = "LAST_UPDATE_TIMESTAMP")
	private Date lastUpdateTimeStamp;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPowerVoltage() {
		return powerVoltage;
	}

	public void setPowerVoltage(String powerVoltage) {
		this.powerVoltage = powerVoltage;
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

	public String getPowerFrequency() {
		return powerFrequency;
	}

	public void setPowerFrequency(String powerFrequency) {
		this.powerFrequency = powerFrequency;
	}
}
