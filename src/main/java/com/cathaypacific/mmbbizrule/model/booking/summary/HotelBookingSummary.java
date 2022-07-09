package com.cathaypacific.mmbbizrule.model.booking.summary;

import java.util.List;

public class HotelBookingSummary {
	
	private String status;
	
	private List<HotelSummary> details;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<HotelSummary> getDetails() {
		return details;
	}

	public void setDetails(List<HotelSummary> details) {
		this.details = details;
	}
	
}
