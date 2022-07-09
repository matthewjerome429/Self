package com.cathaypacific.mbcommon.enums.baggage;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BaggageUnitEnum {

	BAGGAGE_WEIGHT_UNIT("K"),
	BAGGAGE_PIECE_UNIT("PC");
	
	private String unit;
	
	private BaggageUnitEnum(String unit) {
		this.unit = unit;
	}
	
	@JsonValue
	public String getUnit() {
		return this.unit;
	}
}
