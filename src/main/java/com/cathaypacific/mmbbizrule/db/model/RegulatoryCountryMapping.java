package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@IdClass(RegulatoryCountryMappingKey.class)
@Table(name="tb_regulatory_country_mapping")
public class RegulatoryCountryMapping {
	@Id
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
	
	@Id
	@NotNull
	@Column(name = "reg_type")
	private String regType;
	
	@Id
	@NotNull
	@Column(name = "country_code")
	private String countryCode;
	
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

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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
