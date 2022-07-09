package com.cathaypacific.mmbbizrule.dto.request.rebooking;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class ReBookingAcceptRequestDTO {
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String oneARloc;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<ReBookingAcceptInfoDTO> acceptInfos;
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String acceptFamilyName;
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String acceptGivenName;

	public String getOneARloc() {
		return oneARloc;
	}

	public void setOneARloc(String oneARloc) {
		this.oneARloc = oneARloc;
	}

	public List<ReBookingAcceptInfoDTO> getAcceptInfos() {
		return acceptInfos;
	}

	public void setAcceptInfos(List<ReBookingAcceptInfoDTO> acceptInfos) {
		this.acceptInfos = acceptInfos;
	}

	public String getAcceptFamilyName() {
		return acceptFamilyName;
	}

	public void setAcceptFamilyName(String acceptFamilyName) {
		this.acceptFamilyName = acceptFamilyName;
	}

	public String getAcceptGivenName() {
		return acceptGivenName;
	}

	public void setAcceptGivenName(String acceptGivenName) {
		this.acceptGivenName = acceptGivenName;
	}
	
}
