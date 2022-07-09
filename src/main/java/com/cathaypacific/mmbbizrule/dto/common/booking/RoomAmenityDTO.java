package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

public class RoomAmenityDTO implements Serializable {

	private static final long serialVersionUID = -268841030716670734L;
	
	private String amenityType;
	
	private String code;
	
	private String supplierCode;
	
	private String _text;

	public String getAmenityType() {
		return amenityType;
	}

	public void setAmenityType(String amenityType) {
		this.amenityType = amenityType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String get_text() {
		return _text;
	}

	public void set_text(String _text) {
		this._text = _text;
	}
	
}
