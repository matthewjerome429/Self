package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class SeatDetailDTO implements Serializable{
	
	private static final long serialVersionUID = 8135047124706080860L;
	@ApiModelProperty(value = "passenger id", required = true)
	private String passengerId;
	@ApiModelProperty(value = "segment id", required = true)
	private String segmentId;
	@ApiModelProperty(value = "seat type,  possible value: EXL(extra legroom seat)/ASR(regular seat)", required = true)
	private String seatType;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
}
