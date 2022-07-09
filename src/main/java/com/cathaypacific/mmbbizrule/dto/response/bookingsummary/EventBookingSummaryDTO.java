package com.cathaypacific.mmbbizrule.dto.response.bookingsummary;

import java.io.Serializable;
import java.util.List;

public class EventBookingSummaryDTO implements Serializable{
	
	private static final long serialVersionUID = -2992820749009067467L;
	
	private List<EventSummaryDTO> details;

	public List<EventSummaryDTO> getDetails() {
		return details;
	}

	public void setDetails(List<EventSummaryDTO> details) {
		this.details = details;
	}

}
