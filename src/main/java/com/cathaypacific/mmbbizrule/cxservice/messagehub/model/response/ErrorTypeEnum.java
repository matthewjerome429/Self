package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

public enum ErrorTypeEnum {

	/**
	 * Business Error
	 */
	@SerializedName("E") // for Gson
	BUSERROR("E");

	private String type;

	private ErrorTypeEnum(String type) {

		this.type = type;
	}
	@JsonValue
	public String getType() {
		return this.type;
	}
}