package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_COUNTRY_CURRENCY_MAP")
@IdClass(CountryCurrencyMapKey.class)
public class CountryCurrencyMap {
	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;

	@Id
	@NotNull
	@Column(name = "COUNTRY_CODE", length = 5)
	private String countryCode;

	@NotNull
	@Column(name = "CURRENCY_NAME", length = 5)
	private String currencyName;

	@NotNull
	@Column(name = "CURRENCY_CODE", length = 45)
	private String currencyCode;

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

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}
