package com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

public class PriceDTO implements Serializable {

	private static final long serialVersionUID = -5802784207802513159L;

	@ApiModelProperty(value = "Amount of price", required = true, example = "1000.00")
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "\\d+(\\.\\d{1,3})?", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String amount;

	@ApiModelProperty(value = "Currency of price", required = true, example = "HKD")
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String currency;

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
