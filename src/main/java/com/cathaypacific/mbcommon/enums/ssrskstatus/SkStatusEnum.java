package com.cathaypacific.mbcommon.enums.ssrskstatus;

public enum SkStatusEnum {
	CANCELLED("CC"), CONFIRMED("CF");

	private String code;

	private SkStatusEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
