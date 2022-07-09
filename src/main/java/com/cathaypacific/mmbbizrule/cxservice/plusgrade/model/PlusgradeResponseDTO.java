package com.cathaypacific.mmbbizrule.cxservice.plusgrade.model;

import java.io.Serializable;

public class PlusgradeResponseDTO implements Serializable{

	private static final long serialVersionUID = 3202550971900269547L;

	private String status;
	
	private String offerUrl;
	
	private String modifyUrl;
	
	private String error;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOfferUrl() {
		return offerUrl;
	}

	public void setOfferUrl(String offerUrl) {
		this.offerUrl = offerUrl;
	}

	public String getModifyUrl() {
		return modifyUrl;
	}

	public void setModifyUrl(String modifyUrl) {
		this.modifyUrl = modifyUrl;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
