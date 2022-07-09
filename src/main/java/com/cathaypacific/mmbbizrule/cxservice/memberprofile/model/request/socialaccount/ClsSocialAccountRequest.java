package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.socialaccount;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.UserInformation;

public class ClsSocialAccountRequest {
	
	private String memberNumber;
	
	private String socialChannelCodeFilter;
	
	private String startSocialChannel;
	
	private String applicationName;
	
	private String correlationId;
	
	private UserInformation userInformation;
	
	private String wsseUser;
	
	private String wssePassword;

	public String getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}

	public String getSocialChannelCodeFilter() {
		return socialChannelCodeFilter;
	}

	public void setSocialChannelCodeFilter(String socialChannelCodeFilter) {
		this.socialChannelCodeFilter = socialChannelCodeFilter;
	}

	public String getStartSocialChannel() {
		return startSocialChannel;
	}

	public void setStartSocialChannel(String startSocialChannel) {
		this.startSocialChannel = startSocialChannel;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public UserInformation getUserInformation() {
		return userInformation;
	}

	public void setUserInformation(UserInformation userInformation) {
		this.userInformation = userInformation;
	}

	public String getWsseUser() {
		return wsseUser;
	}

	public void setWsseUser(String wsseUser) {
		this.wsseUser = wsseUser;
	}

	public String getWssePassword() {
		return wssePassword;
	}

	public void setWssePassword(String wssePassword) {
		this.wssePassword = wssePassword;
	}

}
