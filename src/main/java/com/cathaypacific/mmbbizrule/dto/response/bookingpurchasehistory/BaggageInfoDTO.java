package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;

import io.swagger.annotations.ApiModelProperty;

public class BaggageInfoDTO implements Serializable{

	private static final long serialVersionUID = -3465340443181552044L;
	@ApiModelProperty(value = "passenger id", required = true)
	private String passengerId;
	@ApiModelProperty(value = "segment id list (segments of one journey)", required = true)
	private List<String> segmentIds;
	@ApiModelProperty(value = "baggage amount", required = true)
	private BigInteger amount;
	@ApiModelProperty(value = "baggage unit", required = true)
	private BaggageUnitEnum unit;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public List<String> getSegmentIds() {
		return segmentIds;
	}

	public void setSegmentIds(List<String> segmentIds) {
		this.segmentIds = segmentIds;
	}

	public BigInteger getAmount() {
		return amount;
	}

	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

	public BaggageUnitEnum getUnit() {
		return unit;
	}

	public void setUnit(BaggageUnitEnum unit) {
		this.unit = unit;
	}
}
