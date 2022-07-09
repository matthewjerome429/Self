package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class ProductValueDTO implements Serializable {

	private static final long serialVersionUID = 577902753798810249L;

	@ApiModelProperty(value = "Value of product item, this value is used to identify baggage product item", required = true, example = "1")
	private String value;

	@ApiModelProperty(value = "Amount of product item price", required = true, example = "1000.00")
	private String amount;

	@ApiModelProperty(value = "Currency of product item price", required = true, example = "HKD")
	private String currency;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
