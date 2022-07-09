package com.cathaypacific.mmbbizrule.dto.response.umnreformjourney;

import com.cathaypacific.mmbbizrule.dto.common.booking.DepartureArrivalTimeDTO;

public class UMNREFormSegmentDTO {

	private String segmentId;

	private String originPort;
	
	private String destPort;
	
	private String operatingCompany;
	
	private String operatingSegmentNumber;
	
	private String marketingCompany;
	
	private String marketingSegmentNumber;
	
	private String departureTime;
	
	private String arrivalTime;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getOriginPort() {
		return originPort;
	}

	public void setOriginPort(String originPort) {
		this.originPort = originPort;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

	public String getOperatingCompany() {
		return operatingCompany;
	}

	public void setOperatingCompany(String operatingCompany) {
		this.operatingCompany = operatingCompany;
	}

	public String getOperatingSegmentNumber() {
		return operatingSegmentNumber;
	}

	public void setOperatingSegmentNumber(String operatingSegmentNumber) {
		this.operatingSegmentNumber = operatingSegmentNumber;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getMarketingCompany() {
		return marketingCompany;
	}

	public void setMarketingCompany(String marketingCompany) {
		this.marketingCompany = marketingCompany;
	}

	public String getMarketingSegmentNumber() {
		return marketingSegmentNumber;
	}

	public void setMarketingSegmentNumber(String marketingSegmentNumber) {
		this.marketingSegmentNumber = marketingSegmentNumber;
	}

	
}
