package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.socialaccount;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.ClsApiError;

public class ClsSocialAccountResponse {
	
	private String memberNumber;
	
	private String socialChannelCodeFilter;
	
	private String statusCode;
	
	private String startSocialChannel;
	
	private String startNextPageSocialChannel;
	
	private int totalRecords;
	
	private String correlationId;
	
	private List<SocialProfileRecord> socialProfileRecord;
	
	private List<ClsApiError> errors;

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

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStartSocialChannel() {
		return startSocialChannel;
	}

	public void setStartSocialChannel(String startSocialChannel) {
		this.startSocialChannel = startSocialChannel;
	}

	public String getStartNextPageSocialChannel() {
		return startNextPageSocialChannel;
	}

	public void setStartNextPageSocialChannel(String startNextPageSocialChannel) {
		this.startNextPageSocialChannel = startNextPageSocialChannel;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public List<SocialProfileRecord> getSocialProfileRecord() {
		return socialProfileRecord;
	}

	public void setSocialProfileRecord(List<SocialProfileRecord> socialProfileRecord) {
		this.socialProfileRecord = socialProfileRecord;
	}

	public List<ClsApiError> getErrors() {
		return errors;
	}

	public void setErrors(List<ClsApiError> errors) {
		this.errors = errors;
	}
}
