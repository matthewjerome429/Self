package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class CountryCurrencyMapKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;


	@NotNull
	@Column(name = "COUNTRY_CODE", length = 5)
	private String countryCode;
	
	public CountryCurrencyMapKey() {

	}
	
	public CountryCurrencyMapKey(String appCode, String countryCode) {
		super();
		this.appCode = appCode;
		this.countryCode = countryCode;
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

	
}

