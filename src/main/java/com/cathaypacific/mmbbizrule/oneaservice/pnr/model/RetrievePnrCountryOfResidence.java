package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

public class RetrievePnrCountryOfResidence {
	private String countryOfResidence;
    private BigInteger qualifierId;
    
	public String getCountryOfResidence() {
		return countryOfResidence;
	}
	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}
	public BigInteger getQualifierId() {
		return qualifierId;
	}
	public void setQualifierId(BigInteger qualifierId) {
		this.qualifierId = qualifierId;
	}

}
