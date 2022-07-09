package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.enums;

public enum PurchaseProductTypeEnum {
	
	SEAT_EXTRA_LEGROOM("SEAT_EXL"),

	SEAT_ASR_REGULAR("SEAT_ASR_REGULAR"),
	
	SEAT_ASR_WINDOW("SEAT_ASR_WINDOW"),
	
	SEAT_ASR_AISLE("SEAT_ASR_AISLE"),

	BAGGAGE("BAGGAGE"),

	LOUNGE_BUSINESS("LOUNGE_BUSINESS"),

	LOUNGE_FIRST("LOUNGE_FIRST");

	private String type;
	
	PurchaseProductTypeEnum (String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
