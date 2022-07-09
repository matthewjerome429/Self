package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;

import com.cathaypacific.mbcommon.enums.booking.LoungeClass;

import io.swagger.annotations.ApiModelProperty;

public class LoungeInfoDTO implements Serializable {

	private static final long serialVersionUID = -5979751578435309704L;

	@ApiModelProperty(value = "passenger id", required = true)
	private String passengerId;

	@ApiModelProperty(value = "segment id", required = true)
	private String segmentId;

	@ApiModelProperty(
			value = "lounge type,  possible value: BLAC (business lounge access) / FLAC (first lounge access)",
			allowableValues = "BLAC, FLAC", required = true)
	private LoungeClass loungeType;

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

	public LoungeClass getLoungeType() {
		return loungeType;
	}

	public void setLoungeType(LoungeClass loungeType) {
		this.loungeType = loungeType;
	}

}
