package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PhoneInfo implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1299041645668083072L;

	/** country code of the phone number */
	private String countryCode;
	
	/** phone number */
	private String phoneNo;
	
	
	/** Phone Country Number */
	private String phoneCountryNumber;
	
	/** type of this information */
	private String type; 

	/** Whether it is olss contact */
	private boolean olssContact;
	
	/** Whether it is valid phone number*/
	private Boolean isValid;
	
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
	
	public boolean isEmpty(){
		return StringUtils.isEmpty(countryCode) && StringUtils.isEmpty(phoneNo);
	}

	public boolean isOlssContact() {
		return olssContact;
	}

	public void setOlssContact(boolean olssContact) {
		this.olssContact = olssContact;
	}

	public String getPhoneCountryNumber() {
		return phoneCountryNumber;
	}

	public void setPhoneCountryNumber(String phoneCountryNumber) {
		this.phoneCountryNumber = phoneCountryNumber;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

}
