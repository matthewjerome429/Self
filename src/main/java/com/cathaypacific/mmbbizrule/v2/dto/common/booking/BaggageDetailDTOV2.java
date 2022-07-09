package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.math.BigInteger;

import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;

public class BaggageDetailDTOV2 implements Serializable {
	
	private static final long serialVersionUID = -1480410841239762538L;

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
