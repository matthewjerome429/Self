package com.cathaypacific.mbcommon.enums.error;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

public enum ErrorTypeEnum {
	/**
	 * Business Error
	 */
	@SerializedName("E") // for Gson
	BUSERROR("E"),
	/**
	 * Warning
	 */
	@SerializedName("W") // for Gson
	WARNING("W"),
	/**
	 * System Error
	 */
	@SerializedName("S") // for Gson
	SYSERROR("S"),
	/**
	 * Validation
	 */
	@SerializedName("V") // for Gson
	VALIDATION("V"),

	/**
	 * The message before do update.
	 */
	@SerializedName("L") // for Gson
	REMINDER("L"),
	
	/**
	 * The message is used only in backend
	 */
	@SerializedName("I") // for Gson
	Inner("I"),
	
	/**
	 * Display error
	 */
	@SerializedName("D") // for Gson
	DISPLAY("D");
	
	private String type;

	private ErrorTypeEnum(String type) {

		this.type = type;
	}
	@JsonValue
	public String getType() {
		return this.type;
	}

}
