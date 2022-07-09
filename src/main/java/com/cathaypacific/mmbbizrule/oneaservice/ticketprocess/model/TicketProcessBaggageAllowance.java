package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model;

import java.math.BigInteger;

import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;

public class TicketProcessBaggageAllowance {
	
	private BigInteger number;
	
	private BaggageUnitEnum unit;

	public BigInteger getNumber() {
		return number;
	}

	public void setNumber(BigInteger number) {
		this.number = number;
	}

	public BaggageUnitEnum getUnit() {
		return unit;
	}

	public void setUnit(BaggageUnitEnum unit) {
		this.unit = unit;
	}

	
}
