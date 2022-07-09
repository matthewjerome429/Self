package com.cathaypacific.mbcommon.enums.upgrade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UpgradeBidStatus {
	
	REQUEST("R"), CALCULATE("C"), SUCCESS("S"), FAILURE("F");

	private String code;

	private UpgradeBidStatus(String code) {
		this.code = code;
	}

	@JsonValue
	public String getCode() {
		return this.code;
	}
	
	@JsonCreator
	public static UpgradeBidStatus parse(String code) {
		for (UpgradeBidStatus value : values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		// check name if cannot find by code
		return UpgradeBidStatus.valueOf(code);
	}
	
}
