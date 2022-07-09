package com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class PriceDTO implements Serializable{

	private static final long serialVersionUID = -7793967475609210193L;
	@ApiModelProperty(value = "price amount", required = true)
	private BigDecimal amount;
	@ApiModelProperty(value = "price currency", required = true)
	private String currency;

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
