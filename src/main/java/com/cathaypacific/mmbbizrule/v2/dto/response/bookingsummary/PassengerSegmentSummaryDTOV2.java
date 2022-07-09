package com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class PassengerSegmentSummaryDTOV2 implements Serializable{
	
	private static final long serialVersionUID = -2924530101803090001L;
	// passenger id
	private String passengerId;
	// segment id
	private String segmentId;

	@ApiModelProperty(value = "the carrier of reverse checkin.", required = false)
	private String reverseCheckinCarrier;

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
	
	public String getReverseCheckinCarrier() {
		return reverseCheckinCarrier;
	}

	public void setReverseCheckinCarrier(String reverseCheckinCarrier) {
		this.reverseCheckinCarrier = reverseCheckinCarrier;
	}
}
