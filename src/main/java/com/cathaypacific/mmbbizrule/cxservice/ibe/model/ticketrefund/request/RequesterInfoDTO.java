package com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request;

public class RequesterInfoDTO {
	
	private String requesterLastName;
	
	private String requesterFirstName;
	
	private String requesterEmail;
	
	private String reconfirmRequesterEmail;

	public String getRequesterLastName() {
		return requesterLastName;
	}

	public void setRequesterLastName(String requesterLastName) {
		this.requesterLastName = requesterLastName;
	}

	public String getRequesterFirstName() {
		return requesterFirstName;
	}

	public void setRequesterFirstName(String requesterFirstName) {
		this.requesterFirstName = requesterFirstName;
	}

	public String getRequesterEmail() {
		return requesterEmail;
	}

	public void setRequesterEmail(String requesterEmail) {
		this.requesterEmail = requesterEmail;
	}

	public String getReconfirmRequesterEmail() {
		return reconfirmRequesterEmail;
	}

	public void setReconfirmRequesterEmail(String reconfirmRequesterEmail) {
		this.reconfirmRequesterEmail = reconfirmRequesterEmail;
	}
	
}
