package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.domain;

import java.util.List;

public class ActionDTO {

	private String action;
	
	private TravellerInfo travellerInfo;
	
	private Flight flight;
	
	private List<Meta> oldValue;
	
	private List<Meta> newValue;
	
	private String oldStatus;
	
	private String newStatus;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public TravellerInfo getTravellerInfo() {
		return travellerInfo;
	}

	public void setTravellerInfo(TravellerInfo travellerInfo) {
		this.travellerInfo = travellerInfo;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public List<Meta> getOldValue() {
		return oldValue;
	}

	public void setOldValue(List<Meta> oldValue) {
		this.oldValue = oldValue;
	}

	public List<Meta> getNewValue() {
		return newValue;
	}

	public void setNewValue(List<Meta> newValue) {
		this.newValue = newValue;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	
}
