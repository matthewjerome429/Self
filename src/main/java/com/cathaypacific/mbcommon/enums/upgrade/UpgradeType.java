package com.cathaypacific.mbcommon.enums.upgrade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UpgradeType {

	REDEMPTION("R"), BID("B"), BOOKABLEUPGRADE("U");

	private String code;

	private UpgradeType(String code) {
		this.code = code;
	}
	@JsonValue
	public String getCode() {
		return this.code;
	}
	
	@JsonCreator
	public static UpgradeType parse(String code) {
		for (UpgradeType value : values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		//check name if cannot find by code
		return UpgradeType.valueOf(code);
	}
}
