package com.cathaypacific.mmbbizrule.model.profile;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProfilePhoneInfo {
	
	/** country code of phone number */
	private String countryCode;
	
	/** phone number */
	private String phoneNo;
	
	/** Phone Country Number */
	private String PhoneCountryNumber;
	
	/** type of this information */
	private String type; 
	
	/** Whether it is olss contact */
	private boolean olssContact;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonIgnore
	public Boolean isEmpty() {
		
		if(StringUtils.isEmpty(countryCode) && StringUtils.isEmpty(phoneNo) && StringUtils.isEmpty(type)) {
			
			return true;
		} else {
			return false;
		}
	}

	public boolean isOlssContact() {
		return olssContact;
	}

	public void setOlssContact(boolean olssContact) {
		this.olssContact = olssContact;
	}

	public String getPhoneCountryNumber() {
		return PhoneCountryNumber;
	}

	public void setPhoneCountryNumber(String phoneCountryNumber) {
		PhoneCountryNumber = phoneCountryNumber;
	}

}
