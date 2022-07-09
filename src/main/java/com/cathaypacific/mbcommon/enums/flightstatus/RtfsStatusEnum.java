package com.cathaypacific.mbcommon.enums.flightstatus;

import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;
@ApiModel
public enum RtfsStatusEnum {

	CANCELLED("CC"), REROUTED("RR"), ARRIVED("AR"),ONTIME("OT"),DELAYED("DL"),OTHERSTATUS("OS");

	private String code;

	private RtfsStatusEnum(String code) {
		this.code = code;
	}
	@JsonValue
	public String getCode() {
		return code;
	}

}
