package com.cathaypacific.mmbbizrule.model.journey;

public class JourneySegment {

	private String segmentId;

	/** code share flight Number */
	private String marketSegmentNumber;

	/** code share flight Provider */
	private String marketCompany;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getMarketSegmentNumber() {
		return marketSegmentNumber;
	}

	public void setMarketSegmentNumber(String marketSegmentNumber) {
		this.marketSegmentNumber = marketSegmentNumber;
	}

	public String getMarketCompany() {
		return marketCompany;
	}

	public void setMarketCompany(String marketCompany) {
		this.marketCompany = marketCompany;
	}
	
	
}
