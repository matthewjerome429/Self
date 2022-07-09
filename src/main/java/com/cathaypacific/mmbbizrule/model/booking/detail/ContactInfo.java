package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class ContactInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3833542249825930862L;
	/** phone info */
	private PhoneInfo phoneInfo;
	/** email - this is original email logic which will be displayed to pax */
	private Email email;
	/** notification email - this is the email address which can be used to send email to pax */
	private List<Email> notificationEmails;
	
	public PhoneInfo getPhoneInfo() {
		return phoneInfo;
	}
	public PhoneInfo findPhoneInfo() {
		if(phoneInfo == null){
			phoneInfo = new PhoneInfo();
		}
		return phoneInfo;
	}
	public void setPhoneInfo(PhoneInfo phoneInfo) {
		this.phoneInfo = phoneInfo;
	}
	public Email getEmail() {
		return email;
	}
	public Email findEmail() {
		if(email == null){
			email = new Email();
		}
		return email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}
	public List<Email> getNotificationEmails() {
		return notificationEmails;
	}
	public void setNotificationEmails(List<Email> notificationEmails) {
		this.notificationEmails = notificationEmails;
	}
	public List<Email> findNotificationEmails() {
		if (notificationEmails == null) {
			notificationEmails = Lists.newArrayList();
		}
		return notificationEmails;
	}
	
}
