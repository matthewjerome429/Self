package com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary;

import java.io.Serializable;
import java.util.List;

public class EventBookingSummaryDTOV2 implements Serializable{
	
	private static final long serialVersionUID = -2992820749009067467L;
	
	private List<EventSummaryDTOV2> details;

	public List<EventSummaryDTOV2> getDetails() {
		return details;
	}

	public void setDetails(List<EventSummaryDTOV2> details) {
		this.details = details;
	}

}
