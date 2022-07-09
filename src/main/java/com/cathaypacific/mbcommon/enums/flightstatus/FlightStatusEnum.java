package com.cathaypacific.mbcommon.enums.flightstatus;

import org.apache.commons.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
@ApiModel
public enum FlightStatusEnum {

	CANCELLED("CC"), WAITLISTED("WL"), CONFIRMED("CF"),STANDBY("SA");

	private String code;

	private FlightStatusEnum(String code) {
		this.code = code;
	}
	@JsonValue
	public String getCode() {
		return code;
	}

	@JsonCreator
	public static FlightStatusEnum getItem(String code) {
		for (FlightStatusEnum item : values()) {
			if (StringUtils.equals(item.getCode(), code)) {
				return item;
			}
		}
		return null;
	}


}
