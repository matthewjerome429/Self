package com.cathaypacific.mbcommon.enums.upgrade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UpgradeRedStatus {
	
	WAITLIST("W"), CONFIRMED("C");

	private String code;

	private UpgradeRedStatus(String code) {
		this.code = code;
	}
	@JsonValue
	public String getCode() {
		return this.code;
	}
	
	@JsonCreator
	public static UpgradeRedStatus parse(String code) {
		for (UpgradeRedStatus value : values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		//check name if cannot find by code
		return UpgradeRedStatus.valueOf(code);
	}
}
