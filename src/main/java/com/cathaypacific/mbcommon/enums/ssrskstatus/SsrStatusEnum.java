package com.cathaypacific.mbcommon.enums.ssrskstatus;

public enum SsrStatusEnum {
	CANCELLED("CC"), CONFIRMED("CF");

	private String code;

	private SsrStatusEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
