package com.cathaypacific.mmbbizrule.cxservice.aep;

public enum AEPProductIdEnum {

	SEAT("1"),

	BAGGAGE_COMMON("2"),

	BAGGAGE_USA("3"),

	BAGGAGE_NZL("4");

	private String id;

	private AEPProductIdEnum(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
