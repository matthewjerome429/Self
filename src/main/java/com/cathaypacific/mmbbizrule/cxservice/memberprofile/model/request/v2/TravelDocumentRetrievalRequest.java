package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.v2;

public class TravelDocumentRetrievalRequest {

	private String memberIdOrUsername;
	
	private String applicationName;
	
	private String correlationId;

	public String getMemberIdOrUsername() {
		return memberIdOrUsername;
	}

	public void setMemberIdOrUsername(String memberIdOrUsername) {
		this.memberIdOrUsername = memberIdOrUsername;
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
	
}
