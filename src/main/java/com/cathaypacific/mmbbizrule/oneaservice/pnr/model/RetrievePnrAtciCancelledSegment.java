package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

public class RetrievePnrAtciCancelledSegment {
	
	/** Format of time*/
	public static final String TIME_FORMAT = "MM-dd";
	
	// since the segment has been removed from PNR and is builded by RM item, this is a mocked id, e.g. "1M"
	private String segmentId;
	// format: "MM-DD" parsed from RM directly
	private String departureDate;
	
	private String operateFlightNumber;
	
	private String operateCompany;
	
	private String originPort;
	
	private String destPort;
	
	private RetrievePnrRebookInfo rebookInfo;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getOperateFlightNumber() {
		return operateFlightNumber;
	}

	public void setOperateFlightNumber(String operateFlightNumber) {
		this.operateFlightNumber = operateFlightNumber;
	}

	public String getOperateCompany() {
		return operateCompany;
	}

	public void setOperateCompany(String operateCompany) {
		this.operateCompany = operateCompany;
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

	public RetrievePnrRebookInfo getRebookInfo() {
		return rebookInfo;
	}

	public void setRebookInfo(RetrievePnrRebookInfo rebookInfo) {
		this.rebookInfo = rebookInfo;
	} 
}
