package com.cathaypacific.mmbbizrule.dto.response.bookingsummary;

import java.io.Serializable;
import java.util.List;

public class HotelBookingSummaryDTO implements Serializable{

	private static final long serialVersionUID = -950736652052088346L;

	private String status;
	
	private List<HotelSummaryDTO> details;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<HotelSummaryDTO> getDetails() {
		return details;
	}

	public void setDetails(List<HotelSummaryDTO> details) {
		this.details = details;
	}
	
}
