package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

public class RetrievePnrEmrContactInfo {
	/** name of emergency contact person */
	private String name;	
	/** country code of phone number */
	private String countryCode;
	/** emergency phone number */
	private String phoneNumber;
	/** OT number in pnr */
	private BigInteger qualifierId;
	
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
	public BigInteger getQualifierId() {
		return qualifierId;
	}
	public void setQualifierId(BigInteger qualifierId) {
		this.qualifierId = qualifierId;
	}
	
}
