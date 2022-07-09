package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class Email implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1955429103457922903L;

	/** email address */
	private String emailAddress;
	
	/** type of this information */
	private String type;
	
	/** Whether it is olss contact */
	private boolean olssContact;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isEmpty(){
		return StringUtils.isEmpty(emailAddress);
	}

	public boolean isOlssContact() {
		return olssContact;
	}

	public void setOlssContact(boolean olssContact) {
		this.olssContact = olssContact;
	}
}
