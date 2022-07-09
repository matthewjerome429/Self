package com.cathaypacific.mmbbizrule.cxservice.eods.model;

public class EODSBookingSegment {
	
	/** Format of time*/
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";
	
	private String segmentNo;
	
	/** yyyy-MM-dd HH:mm */
	private String depDateTime;
	
	/** yyyy-MM-dd HH:mm */
	private String arrDateTime;

	/** The origin. */
	private String originPort;

	/** The destination. */
	private String destPort;

	/** original flight Number */
	private String number;

	/** original flight Provider */
	private String company;

	private String status;
	
	public String getDepDateTime() {
		return depDateTime;
	}

	public void setDepDateTime(String depDateTime) {
		this.depDateTime = depDateTime;
	}

	public String getArrDateTime() {
		return arrDateTime;
	}

	public void setArrDateTime(String arrDateTime) {
		this.arrDateTime = arrDateTime;
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSegmentNo() {
		return segmentNo;
	}

	public void setSegmentNo(String segmentNo) {
		this.segmentNo = segmentNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
