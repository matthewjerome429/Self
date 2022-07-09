package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.List;

public class RetrievePnrApContactPhone extends RetrievePnrContactPhone{
	
	private List<String> passengerIds;

	public List<String> getPassengerIds() {
		return passengerIds;
	}

	public void setPassengerIds(List<String> passengerIds) {
		this.passengerIds = passengerIds;
	}

}
