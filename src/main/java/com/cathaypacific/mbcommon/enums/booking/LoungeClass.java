package com.cathaypacific.mbcommon.enums.booking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Lounge access class.
 */
public enum LoungeClass {

	 BUSINESS("BLAC", "B"),

	 FIRST("FLAC", "F");

	/**
	 * Lounge class code.
	 */
	private String code;

	/**
	 * Lounge class short code.
	 */
	private String shortCode;

	private LoungeClass(String code, String shortCode) {
		this.code = code;
		this.shortCode = shortCode;
	}
	
	@JsonValue
	public String code() {
		return code;
	}

	public String shortCode() {
		return shortCode;
	}

	@Override
	public String toString() {
		return code;
	}
	
	@JsonCreator
	public static LoungeClass parse(String code) {
		LoungeClass lounge = parseCode(code);
		return lounge != null ? lounge : parseShortCode(code);
	}

	public static LoungeClass parseCode(String code) {
		for (LoungeClass value : values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		return null;
	}

	public static LoungeClass parseShortCode(String shortCode) {
		for (LoungeClass value : values()) {
			if (value.shortCode.equals(shortCode)) {
				return value;
			}
		}
		return null;
	}

}
