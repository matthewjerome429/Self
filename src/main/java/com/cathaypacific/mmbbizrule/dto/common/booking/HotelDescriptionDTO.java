package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

public class HotelDescriptionDTO implements Serializable{
	
	private static final long serialVersionUID = -3785739877285707347L;

	private String code;
	
	private String text;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
