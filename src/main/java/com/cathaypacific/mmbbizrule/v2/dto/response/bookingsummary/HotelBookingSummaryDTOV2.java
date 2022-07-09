package com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary;

import java.io.Serializable;
import java.util.List;

public class HotelBookingSummaryDTOV2 implements Serializable{

	private static final long serialVersionUID = -950736652052088346L;

	private String status;
	
	private List<HotelSummaryDTOV2> details;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<HotelSummaryDTOV2> getDetails() {
		return details;
	}

	public void setDetails(List<HotelSummaryDTOV2> details) {
		this.details = details;
	}
	
}
