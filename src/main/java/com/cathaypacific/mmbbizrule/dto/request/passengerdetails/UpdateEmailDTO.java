package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.Updated;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UpdateEmailDTO extends Updated{

	private static final long serialVersionUID = 7390419105377389237L;

	@NotNull
	@Pattern(regexp = "^([\\w\\.\\-\\_])+@(([\\w\\-\\_])+\\.)+(\\w)+$", groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String email;
	
	@ApiModelProperty("convert the existing email to OLSS contact. If this flag is true, won't update a new email, only update the existing one to OLSS contact")
	private Boolean convertToOlssContactInfo;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getConvertToOlssContactInfo() {
		return convertToOlssContactInfo;
	}

	public void setConvertToOlssContactInfo(Boolean convertToOlssContactInfo) {
		this.convertToOlssContactInfo = convertToOlssContactInfo;
	}
	
}
