package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class SegmentInfoDTO implements Serializable{

	private static final long serialVersionUID = -7351929628588561454L;
	@ApiModelProperty(value = "segment id", required = true)
	private String segmentId;
	/** format: YYYY-MM-DD */
	@ApiModelProperty(value = "segment departure time, format: YYYY-MM-DD HH:mm", required = true)
	private String departureTime;
	@ApiModelProperty(value = "origin port", required = true)
	private String originPort;
	@ApiModelProperty(value = "destination port", required = true)
	private String destPort;
	@ApiModelProperty(value = "company id", required = true)
	private String companyId;
	@ApiModelProperty(value = "segment number", required = true)
	private String segmentNumber;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSegmentNumber() {
		return segmentNumber;
	}

	public void setSegmentNumber(String segmentNumber) {
		this.segmentNumber = segmentNumber;
	}
	
}
