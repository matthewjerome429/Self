package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigDecimal;

/**
 * on hold info 
 * @author zilong.bu
 *
 */
public class RetrievePnrOnHoldInfo extends DataElement{
	
	private boolean hasOnHoldRemark;
	
	private BigDecimal amount;
	
	private String currency;
	
	public boolean isHasOnHoldRemark() {
		return hasOnHoldRemark;
	}

	public void setHasOnHoldRemark(boolean hasOnHoldRemark) {
		this.hasOnHoldRemark = hasOnHoldRemark;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	

}
