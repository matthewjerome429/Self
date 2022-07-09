package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class WeatherHistoricalAvgKey implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
    @Column(name="app_code", length = 5)
	private String appCode;

	@NotNull
    @Column(name="port_code", length = 5)
	private String portCode;
	

	public WeatherHistoricalAvgKey() {

	}

	public WeatherHistoricalAvgKey(String appCode, String portCode) {
		this.appCode = appCode;
		this.portCode = portCode;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getPortCode() {
		return portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
}
