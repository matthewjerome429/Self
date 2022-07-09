package com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.model;

import java.util.List;

public class PnrSearchSegment {
	/** Format of time*/
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";
	
	private String segmentId;
	
	 /** yyyy-MM-dd HH:mm*/
    private String depDateTime;
    /** yyyy-MM-dd HH:mm */
    private String arrDateTime;
    
    /** The origin. */
    private String originPort;
    
    /** The destination. */
    private String destPort;
    
    private Integer numberOfStops;
    
    /** original flight Number */
    private String number;
    
    /** original flight Provider */
    private String company;
    
    private List<String> status;

    
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

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public Integer getNumberOfStops() {
		return numberOfStops;
	}

	public void setNumberOfStops(Integer numberOfStops) {
		this.numberOfStops = numberOfStops;
	}

}
