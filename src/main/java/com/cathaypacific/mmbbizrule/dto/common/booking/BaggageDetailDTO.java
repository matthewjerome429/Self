package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.math.BigInteger;

import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;

public class BaggageDetailDTO {

	/** amount of baggage allowance */
	private BigInteger amount;
	
	/** unit of the amount */
	private BaggageUnitEnum unit;

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
