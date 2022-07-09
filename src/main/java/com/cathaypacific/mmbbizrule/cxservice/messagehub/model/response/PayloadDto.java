package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import java.io.Serializable;

public class PayloadDto implements Serializable {

	private static final long serialVersionUID = -798238034562980067L;

	private String name;

	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
