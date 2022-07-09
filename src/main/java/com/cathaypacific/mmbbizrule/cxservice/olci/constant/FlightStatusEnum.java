package com.cathaypacific.mmbbizrule.cxservice.olci.constant;

public enum FlightStatusEnum {

	CANCELLED("CANCELLED"),
	FLOWN("FLOWN");
	
	
	private String code;
	private FlightStatusEnum(String code) {
		this.code = code;
	}
	
	public static FlightStatusEnum valueIn(String orgs){
		for (FlightStatusEnum enumz : FlightStatusEnum.values()) {
			if (enumz.getCode().equals(orgs)) {
				return enumz;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
