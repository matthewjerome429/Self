package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ContactInfoDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2505122607173720796L;
	/** phone info */
	private PhoneInfoDTO phoneInfo;
	/** email */
	private EmailDTO email;

	public PhoneInfoDTO getPhoneInfo() {
		return phoneInfo;
	}

	public PhoneInfoDTO findPhoneInfo() {
		if (phoneInfo == null) {
			phoneInfo = new PhoneInfoDTO();
		}
		return phoneInfo;
	}

	public void setPhoneInfo(PhoneInfoDTO phoneInfo) {
		this.phoneInfo = phoneInfo;
	}

	public EmailDTO getEmail() {
		return email;
	}

	public EmailDTO findEmail() {
		if (email == null) {
			email = new EmailDTO();
		}
		return email;
	}

	public void setEmail(EmailDTO email) {
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
