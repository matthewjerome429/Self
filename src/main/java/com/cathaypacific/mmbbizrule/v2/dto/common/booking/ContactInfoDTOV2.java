package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ContactInfoDTOV2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2505122607173720796L;
	/** phone info */
	private PhoneInfoDTOV2 phoneInfo;
	/** email */
	private EmailDTOV2 email;

	public PhoneInfoDTOV2 getPhoneInfo() {
		return phoneInfo;
	}

	public PhoneInfoDTOV2 findPhoneInfo() {
		if (phoneInfo == null) {
			phoneInfo = new PhoneInfoDTOV2();
		}
		return phoneInfo;
	}

	public void setPhoneInfo(PhoneInfoDTOV2 phoneInfo) {
		this.phoneInfo = phoneInfo;
	}

	public EmailDTOV2 getEmail() {
		return email;
	}

	public EmailDTOV2 findEmail() {
		if (email == null) {
			email = new EmailDTOV2();
		}
		return email;
	}

	public void setEmail(EmailDTOV2 email) {
		this.email = email;
	}

	@JsonIgnore
	public Boolean isEmpty() {

		if (phoneInfo == null && email == null) {
			return true;
		} else if (phoneInfo != null && email != null) {
			boolean phoneInfoEmpty = phoneInfo.isEmpty();
			boolean emailEmpty = email.isEmpty();
			return (phoneInfoEmpty && emailEmpty)?true:false;
		} else if (phoneInfo == null) {
			return email.isEmpty()? true: false;
		} else {
			return phoneInfo.isEmpty()?true:false;
		}
	}
}
