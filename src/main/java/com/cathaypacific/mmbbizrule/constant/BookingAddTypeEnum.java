package com.cathaypacific.mmbbizrule.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;

@ApiModel
public enum BookingAddTypeEnum {
	
	ONEA("FQTV"), EODS("CUST");
	
	private String type;

	private BookingAddTypeEnum(String type) {
		this.type = type;
	}
	
	@JsonValue
	public String getType() {
		return type;
	}
	
}
