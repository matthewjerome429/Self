package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2;

import java.util.List;

public class TravelDocumentRetrievalResponse {

	private String statusCode;
	
	private List<Error> errors;
	
	private String memberIdOrUsername;
	
	// @JsonFormat(pattern="DDMMYYYY")
	private String dateOfBirth;
	
	private CustomerTravelerInformationResponse customerTravelerInformation;
	
	private List<CompanionTravelerInformationResponse> companionTravelerInformation;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public String getMemberIdOrUsername() {
		return memberIdOrUsername;
	}

	public void setMemberIdOrUsername(String memberIdOrUsername) {
		this.memberIdOrUsername = memberIdOrUsername;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public CustomerTravelerInformationResponse getCustomerTravelerInformation() {
		return customerTravelerInformation;
	}

	public void setCustomerTravelerInformation(CustomerTravelerInformationResponse customerTravelerInformation) {
		this.customerTravelerInformation = customerTravelerInformation;
	}

	public List<CompanionTravelerInformationResponse> getCompanionTravelerInformation() {
		return companionTravelerInformation;
	}

	public void setCompanionTravelerInformation(List<CompanionTravelerInformationResponse> companionTravelerInformation) {
		this.companionTravelerInformation = companionTravelerInformation;
	}
	
}
