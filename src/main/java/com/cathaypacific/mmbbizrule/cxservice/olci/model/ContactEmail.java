package com.cathaypacific.mmbbizrule.cxservice.olci.model;

public class ContactEmail extends Contact {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = 3295387249369702739L;

	/** The email address of passenger level */
	private String emailAddress;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
