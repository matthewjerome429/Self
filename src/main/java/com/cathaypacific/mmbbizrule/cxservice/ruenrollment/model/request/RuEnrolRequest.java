package com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request;

import java.util.List;

public class RuEnrolRequest {

	private String applicationName;
	
	private String correlationId;
	
	private RuEnrolPromotionConsent promotionConsent;
	
	private RuEnrolMemberProfile memberProfile;
	
	private RuEnrolMemberProfileDetails memberProfileDetails;
	
	private List<CommunicationPref> communicationPrefs;

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

	public RuEnrolPromotionConsent getPromotionConsent() {
		return promotionConsent;
	}

	public void setPromotionConsent(RuEnrolPromotionConsent promotionConsent) {
		this.promotionConsent = promotionConsent;
	}

	public RuEnrolMemberProfile getMemberProfile() {
		return memberProfile;
	}

	public void setMemberProfile(RuEnrolMemberProfile memberProfile) {
		this.memberProfile = memberProfile;
	}

	public RuEnrolMemberProfileDetails getMemberProfileDetails() {
		return memberProfileDetails;
	}

	public void setMemberProfileDetails(RuEnrolMemberProfileDetails memberProfileDetails) {
		this.memberProfileDetails = memberProfileDetails;
	}

	public List<CommunicationPref> getCommunicationPrefs() {
		return communicationPrefs;
	}

	public void setCommunicationPrefs(List<CommunicationPref> communicationPrefs) {
		this.communicationPrefs = communicationPrefs;
	}

}
