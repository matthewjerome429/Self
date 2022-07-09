package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class PowerSocketTypeKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4096907318826770907L;

	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;

	@NotNull
	@Column(name = "COUNTRY_CODE", length = 5)
	private String countryCode;
	
	@NotNull
	@Column(name = "SOCKET_TYPE", length = 5)
	private String socketType;

	public PowerSocketTypeKey(String appCode, String countryCode, String socketType) {
		this.appCode = appCode;
		this.countryCode = countryCode;
		this.socketType = socketType;
	}

	public PowerSocketTypeKey() {

	}

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

	public String getSocketType() {
		return socketType;
	}

	public void setSocketType(String socketType) {
		this.socketType = socketType;
	}
	
}
