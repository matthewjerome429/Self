package com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.response;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.ClsApiError;

public class RuEnrolResponse {
	
	private RuEnrolCustomer customer;
	
	private String statusCode;
	
	private String internetClientIp;
	
	private String correlationId;
	
	private List<RuEnrolCommunicationPref> communicationPrefs;
	
	private List<ClsApiError> errors;

	public RuEnrolCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(RuEnrolCustomer customer) {
		this.customer = customer;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getInternetClientIp() {
		return internetClientIp;
	}

	public void setInternetClientIp(String internetClientIp) {
		this.internetClientIp = internetClientIp;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public List<RuEnrolCommunicationPref> getCommunicationPrefs() {
		return communicationPrefs;
	}

	public void setCommunicationPrefs(List<RuEnrolCommunicationPref> communicationPrefs) {
		this.communicationPrefs = communicationPrefs;
	}

	public List<ClsApiError> getErrors() {
		return errors;
	}

	public void setErrors(List<ClsApiError> errors) {
		this.errors = errors;
	}
}
