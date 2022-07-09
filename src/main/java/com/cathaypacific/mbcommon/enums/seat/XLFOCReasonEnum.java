package com.cathaypacific.mbcommon.enums.seat;

import com.fasterxml.jackson.annotation.JsonValue;

public enum XLFOCReasonEnum {
	
	IS_TOP_TIER("TOP_TIER"),
	SPECIAL_SK_FOUND("XLSK_FOUND");
	
	private String reason;
	
	private XLFOCReasonEnum(String reason) {
		this.reason = reason;
	}
	
	@JsonValue
	public String getReason() {
		return this.reason;
	}	
}
