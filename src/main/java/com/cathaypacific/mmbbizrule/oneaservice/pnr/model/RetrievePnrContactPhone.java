package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

public class RetrievePnrContactPhone {
	
	/** The phone number of passenger level: phoneNumber. */ 
	private String phoneNumber;

	
	/** The type of the Phone information */
	private String type;
	
	/** The company of the Phone information */
	private String companyId;
	
	/** Whether it is olss contact */
	private boolean olssContact;
	
	/** OT number **/
	private BigInteger qualifierId;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isOlssContact() {
		return olssContact;
	}

	public void setOlssContact(boolean olssContact) {
		this.olssContact = olssContact;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public boolean isEmpty(){
		return StringUtils.isEmpty(phoneNumber) 
				&& StringUtils.isEmpty(type) && StringUtils.isEmpty(companyId) && qualifierId == null;
	}
}
