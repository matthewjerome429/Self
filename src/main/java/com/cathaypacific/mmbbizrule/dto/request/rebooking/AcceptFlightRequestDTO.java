package com.cathaypacific.mmbbizrule.dto.request.rebooking;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class AcceptFlightRequestDTO {
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<AcceptInfoDTO> acceptInfos;
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String acceptFamilyName;
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String acceptGivenName;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<AcceptInfoDTO> getAcceptInfos() {
		return acceptInfos;
	}

	public void setAcceptInfos(List<AcceptInfoDTO> acceptInfos) {
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
