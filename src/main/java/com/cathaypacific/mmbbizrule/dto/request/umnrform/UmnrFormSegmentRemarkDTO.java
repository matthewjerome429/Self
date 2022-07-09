package com.cathaypacific.mmbbizrule.dto.request.umnrform;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.google.common.collect.Lists;

public class UmnrFormSegmentRemarkDTO {

	@NotBlank(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String flightNumber;
	
	@NotBlank(message= ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String flightDate;
	
	@Valid
	private List<UmnrFormPortInfoRemarkDTO> portInfo;

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}

	public List<UmnrFormPortInfoRemarkDTO> getPortInfo() {
		if (portInfo == null) {
			portInfo = Lists.newArrayList();
		}
		return portInfo;
	}

	public void setPortInfo(List<UmnrFormPortInfoRemarkDTO> portInfo) {
		this.portInfo = portInfo;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

}
