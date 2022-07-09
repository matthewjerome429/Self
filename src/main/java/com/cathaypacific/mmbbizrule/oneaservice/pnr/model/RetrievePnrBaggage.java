package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

public class RetrievePnrBaggage {
	/** amount of baggage allowance */
	private BigInteger pcAmount;
	
	private BigInteger weightAmount;
	
	private String companyId;
	
	private RetrievePnrPaymentInfo paymentInfo;
	
	public BigInteger getPcAmount() {
		return pcAmount;
	}
	public void setPcAmount(BigInteger pcAmount) {
		this.pcAmount = pcAmount;
	}
	public BigInteger getWeightAmount() {
		return weightAmount;
	}
	public void setWeightAmount(BigInteger weightAmount) {
		this.weightAmount = weightAmount;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public RetrievePnrPaymentInfo getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(RetrievePnrPaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
	
}
