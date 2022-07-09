package com.cathaypacific.mmbbizrule.cxservice.oj.model.member;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.oj.model.RetrieveOJBookingResponse;


public class RetrieveOJBookingDetailResponse implements Serializable{

	private static final long serialVersionUID = 6473159786814603386L;
	
	private List<RetrieveOJBookingResponse> retrieveOJBookingResponses;

	public List<RetrieveOJBookingResponse> getRetrieveOJBookingResponses() {
		return retrieveOJBookingResponses;
	}

	public void setRetrieveOJBookingResponses(List<RetrieveOJBookingResponse> retrieveOJBookingResponses) {
		this.retrieveOJBookingResponses = retrieveOJBookingResponses;
	}
	
}
