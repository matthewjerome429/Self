package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.math.BigDecimal;

public class PaymentInfoDTOV2 {
	
	private String qualifierId;
	
	private BigDecimal amount;

	private String currency;

	private String ticket;

	private String date;

	private String officeId;

	public String getQualifierId() {
		return qualifierId;
	}

	public void setQualifierId(String qualifierId) {
		this.qualifierId = qualifierId;
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
	
	

}
