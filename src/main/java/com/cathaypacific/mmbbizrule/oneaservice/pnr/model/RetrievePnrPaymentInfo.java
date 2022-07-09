package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigDecimal;

public class RetrievePnrPaymentInfo implements Cloneable{

	private String qualifierId;
	
	private BigDecimal amount;

	private String currency;

	private String ticket;

	private String date;

	private String officeId;

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

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getQualifierId() {
		return qualifierId;
	}

	public void setQualifierId(String qualifierId) {
		this.qualifierId = qualifierId;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
