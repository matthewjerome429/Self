package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;


public class PhoneInfoDTOV2 implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7781725332913636126L;	
	/** country code of phone number */
	@ApiModelProperty(value = "the country code of phone number.", required = true)
	private String countryCode;
	
	/** phone number */
	private String phoneNo;
	
	/** Phone Country Number */
	private String PhoneCountryNumber;
	
	/** type of this information */
	private String type; 
	
	/** Whether it is olss contact */
	@ApiModelProperty(value = "if true the phone information is from olss contact.", required = false)
	private boolean olssContact;
	
	@ApiModelProperty(value = "if true the phone information is from olss contact.", required = false)
	private Boolean phoneNumberMasked;
	
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

	public Boolean getPhoneNumberMasked() {
		return phoneNumberMasked;
	}

	public void setPhoneNumberMasked(Boolean phoneNumberMasked) {
		this.phoneNumberMasked = phoneNumberMasked;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

}
