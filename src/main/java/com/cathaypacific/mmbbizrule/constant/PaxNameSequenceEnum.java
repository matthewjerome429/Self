package com.cathaypacific.mmbbizrule.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;
@ApiModel
public enum PaxNameSequenceEnum {
	LF("LastNameAndFirstName"), FG("FamilyNameAndGivenName");

	private String code;

	private PaxNameSequenceEnum(String code) {
		this.code = code;
	}
	@JsonValue
	public String getCode() {
		return code;
	}
}
