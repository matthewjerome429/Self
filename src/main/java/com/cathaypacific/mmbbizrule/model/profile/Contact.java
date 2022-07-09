package com.cathaypacific.mmbbizrule.model.profile;

public class Contact {

	/** phone info */
	private ProfilePhoneInfo phoneInfo;
	
	/** email */
	private ProfileEmail email;

	public ProfilePhoneInfo getPhoneInfo() {
		return phoneInfo;
	}

	public void setPhoneInfo(ProfilePhoneInfo phoneInfo) {
		this.phoneInfo = phoneInfo;
	}

	public ProfileEmail getEmail() {
		return email;
	}

	public void setEmail(ProfileEmail email) {
		this.email = email;
	}
	
	
}
