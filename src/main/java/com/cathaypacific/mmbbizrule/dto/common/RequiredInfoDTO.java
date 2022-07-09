package com.cathaypacific.mmbbizrule.dto.common;

public class RequiredInfoDTO {

	private Boolean  requirePassenger;
	
	private Boolean  requireTravelDoc;
	
	private Boolean requireFlight;

	public Boolean getRequirePassenger() {
		return requirePassenger;
	}

	public void setRequirePassenger(Boolean requirePassenger) {
		this.requirePassenger = requirePassenger;
	}

	public Boolean getRequireTravelDoc() {
		return requireTravelDoc;
	}

	public void setRequireTravelDoc(Boolean requireTravelDoc) {
		this.requireTravelDoc = requireTravelDoc;
	}

	public Boolean getRequireFlight() {
		return requireFlight;
	}

	public void setRequireFlight(Boolean requireFlight) {
		this.requireFlight = requireFlight;
	}

	@Override
	public String toString() {
		return "RequiredInfoDTO [requirePassenger=" + requirePassenger + ", requireTravelDoc=" + requireTravelDoc
				+ ", requireFlight=" + requireFlight + "]";
	}
	
}
