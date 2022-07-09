package com.cathaypacific.mmbbizrule.v2.dto.request.retrievebooking;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.dto.common.RequiredInfoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RetrievePnrByEncryptedRequestDTOV2 extends RequiredInfoDTO {
	
	@JsonProperty(value ="rloc",required = false)
	@Pattern(regexp = "^[A-Za-z0-9]{6,7}$",message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String rloc;
	
	@JsonProperty(value ="eticket",required = false)
	private String eticket;

	@JsonProperty(value ="familyName",required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "^[A-Za-z ]{1,50}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String familyName;

	@JsonProperty(value ="givenName",required = true)
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "^[A-Za-z ]{1,32}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String givenName;

	public String getRloc() {
		return rloc;
	}
	
	public void setRloc(String rloc) {
		this.rloc = rloc;
	}
	
	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	@Override
	public String toString() {
		return "RetrievePnrByEncryptedRequestDTO [rloc=" + rloc + ", eticket=" + eticket + ", familyName=" + familyName
				+ ", givenName=" + givenName + "]";
	}
}
