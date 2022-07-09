package com.cathaypacific.mbcommon.enums.error;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
@ApiModel
public enum ErrorLevelEnum {
	
	@SerializedName("P") // for Gson
	PASSENGER("P"), // Business Error
	@SerializedName("F") // for Gson
	FLIGHT("F"),
	@SerializedName("B") // for Gson
	BODY("B");

	private String type;

	private ErrorLevelEnum(String type) {
		this.type = type;
	}

	@JsonValue
	public String getType() {
		return this.type;
	}

}
