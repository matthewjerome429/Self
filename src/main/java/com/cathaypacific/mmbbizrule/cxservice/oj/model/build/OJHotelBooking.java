package com.cathaypacific.mmbbizrule.cxservice.oj.model.build;

import java.util.List;

public class OJHotelBooking {
	
	private Boolean isStayed;
	
	private List<OJHotel> details;
	
	private Boolean isCompleted;

	public Boolean isStayed() {
		return isStayed;
	}

	public void setIsStayed(Boolean isStayed) {
		this.isStayed = isStayed;
	}

	public List<OJHotel> getDetails() {
		return details;
	}

	public void setDetails(List<OJHotel> details) {
		this.details = details;
	}

	public Boolean isCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	
}
