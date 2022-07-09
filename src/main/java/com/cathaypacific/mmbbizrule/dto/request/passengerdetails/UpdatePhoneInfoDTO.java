package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UpdatePhoneInfoDTO extends PhoneInfoDTO{
	
	private static final long serialVersionUID = 4733063906567759802L;
	
	private String phoneType;
	
	@ApiModelProperty("convert the existing phone info to OLSS contact. If this flag is true, won't update a new phone info, only update the existing one to OLSS contact")
	private Boolean convertToOlssContactInfo;
	
	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public Boolean getConvertToOlssContactInfo() {
		return convertToOlssContactInfo;
	}

	public void setConvertToOlssContactInfo(Boolean convertToOlssContactInfo) {
		this.convertToOlssContactInfo = convertToOlssContactInfo;
	}
	
}
