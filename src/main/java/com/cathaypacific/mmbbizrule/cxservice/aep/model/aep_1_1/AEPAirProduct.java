package com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1;

import java.util.List;

public class AEPAirProduct {

	private List<AEPSegment> segments;

	private List<AEPPassengerWithName> passengers;

	public List<AEPSegment> getSegments() {
		return segments;
	}

	public void setSegments(List<AEPSegment> segments) {
		this.segments = segments;
	}

	public List<AEPPassengerWithName> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<AEPPassengerWithName> passengers) {
		this.passengers = passengers;
	}

}
