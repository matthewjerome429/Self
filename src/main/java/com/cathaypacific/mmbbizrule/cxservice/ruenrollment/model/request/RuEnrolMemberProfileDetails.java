package com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request;

public class RuEnrolMemberProfileDetails {

	private String email;
	
	private RuEnrolLoginDetails loginDetails;
	
	private RuEnrolPreference preference;
	
	private String enrolmentSource;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RuEnrolLoginDetails getLoginDetails() {
		return loginDetails;
	}

	public void setLoginDetails(RuEnrolLoginDetails loginDetails) {
		this.loginDetails = loginDetails;
	}

	public RuEnrolPreference getPreference() {
		return preference;
	}

	public void setPreference(RuEnrolPreference preference) {
		this.preference = preference;
	}

	public String getEnrolmentSource() {
		return enrolmentSource;
	}

	public void setEnrolmentSource(String enrolmentSource) {
		this.enrolmentSource = enrolmentSource;
	}
	
}
