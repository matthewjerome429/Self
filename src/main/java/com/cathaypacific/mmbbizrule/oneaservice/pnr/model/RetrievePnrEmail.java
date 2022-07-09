package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

public class RetrievePnrEmail {
	private String email;
	private BigInteger qualifierId;
	/** The type of the email information */
	private String type;
	/** The company of the email information */
	private String companyId;
	/** Whether it is olss contact */
	private boolean olssContact;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
		return StringUtils.isEmpty(email) && StringUtils.isEmpty(type) && StringUtils.isEmpty(companyId) && qualifierId == null;
	}
}
