package com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1;

public class AEPSegment {

	private String origin;

	private String destination;

	private String marketingCarrier;

	private Integer flight;

	private String departureDate;

	private Integer segmentRef;

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getMarketingCarrier() {
		return marketingCarrier;
	}

	public void setMarketingCarrier(String marketingCarrier) {
		this.marketingCarrier = marketingCarrier;
	}

	public Integer getFlight() {
		return flight;
	}

	public void setFlight(Integer flight) {
		this.flight = flight;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public Integer getSegmentRef() {
		return segmentRef;
	}

	public void setSegmentRef(Integer segmentRef) {
		this.segmentRef = segmentRef;
	}

}
