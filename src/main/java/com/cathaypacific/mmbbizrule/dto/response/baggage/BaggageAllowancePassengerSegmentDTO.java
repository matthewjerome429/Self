package com.cathaypacific.mmbbizrule.dto.response.baggage;

import java.io.Serializable;

import com.cathaypacific.mmbbizrule.v2.dto.common.booking.BaggageAllowanceDTOV2;

import io.swagger.annotations.ApiModelProperty;

public class BaggageAllowancePassengerSegmentDTO implements Serializable {

	private static final long serialVersionUID = 948144446835466422L;
	
	private String passengerId;
	
	private String segmentId;
	
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

	@ApiModelProperty(value = "Baggage allowance (check in baggage and cabin baggage).", required = false)
	private BaggageAllowanceDTOV2 baggageAllowance;

	public BaggageAllowanceDTOV2 getBaggageAllowance() {
		return baggageAllowance;
	}

	public BaggageAllowanceDTOV2 findBaggageAllowance() {
		if (baggageAllowance == null) {
			baggageAllowance = new BaggageAllowanceDTOV2();
		}
		return baggageAllowance;
	}

	public void setBaggageAllowance(BaggageAllowanceDTOV2 baggageAllowance) {
		this.baggageAllowance = baggageAllowance;
	}

}
