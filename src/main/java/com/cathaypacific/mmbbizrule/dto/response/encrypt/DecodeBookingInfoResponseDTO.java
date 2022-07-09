package com.cathaypacific.mmbbizrule.dto.response.encrypt;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

import io.swagger.annotations.ApiModelProperty;

public class DecodeBookingInfoResponseDTO {

    /** The family name. */
	@ApiModelProperty(value = "1A rloc or GDS rloc", required = true)
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
    /** The family name. */
	@ApiModelProperty(value = "family name", required = true)
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
    private String familyName;
	
    /** The given name. */
	@ApiModelProperty(value = "given name", required = true)
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
    private String givenName;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
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
	
	
	
}
