package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;

import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;

public class BaggageDetail {
	
	/** amount of baggage allowance */
	private BigInteger amount;
	
	/** unit of the amount */
	private BaggageUnitEnum unit;
	
	public BigInteger getAmount() {
		return amount;
	}
	
	public BigInteger findAmount() {
		if(amount == null){
			amount = new BigInteger("0");
		}
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
