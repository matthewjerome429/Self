package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;

/**
 * To store KTN or redress 
 *
 */
public class TS {
	
	private BigInteger qualifierId;
	
	private String number;

	public BigInteger getQualifierId() {
		return qualifierId;
	}

	public void setQualifierId(BigInteger qualifierId) {
		this.qualifierId = qualifierId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}
