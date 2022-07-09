package com.cathaypacific.mmbbizrule.dto.request.seatmap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

public class SeatMapRequestDTO {
	
	@ApiModelProperty(required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@ApiModelProperty(required = false)
	private String currency;
	
	@ApiModelProperty(required = false)
	private PassengerInfo passengerInfo;
	
	@ApiModelProperty(required = true)
	@Valid
	@NotNull
	private SegmentInfo segmentInfo;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public PassengerInfo getPassengerInfo() {
		return passengerInfo;
	}

	public void setPassengerInfo(PassengerInfo passengerInfo) {
		this.passengerInfo = passengerInfo;
	}

	public SegmentInfo getSegmentInfo() {
		return segmentInfo;
	}

	public void setSegmentInfo(SegmentInfo segmentInfo) {
		this.segmentInfo = segmentInfo;
	} 
}
