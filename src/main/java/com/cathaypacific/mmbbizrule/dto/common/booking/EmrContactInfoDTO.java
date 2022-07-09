package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EmrContactInfoDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7070291381972444132L;
	/** name of emergency contact person */
	private String name;
	/** country code of phone number */
	private String countryCode;
	/** emergency phone number */
	private String phoneNumber;
	/** phone number masked */
	private Boolean phoneNumberMasked;
	/** Phone Country Number */
	private String phoneCountryNumber;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Boolean getPhoneNumberMasked() {
		return phoneNumberMasked;
	}
	public void setPhoneNumberMasked(Boolean phoneNumberMasked) {
		this.phoneNumberMasked = phoneNumberMasked;
	}
	public String getPhoneCountryNumber() {
		return phoneCountryNumber;
	}
	public void setPhoneCountryNumber(String phoneCountryNumber) {
		this.phoneCountryNumber = phoneCountryNumber;
	}
	@JsonIgnore
	public Boolean isEmpty() {
		
		return (StringUtils.isEmpty(name) && StringUtils.isEmpty(countryCode) && StringUtils.isEmpty(phoneNumber))?true:false;	
	}
	
}
