package com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.Updated;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UpdateEmailDTOV2 extends Updated{

	private static final long serialVersionUID = 7390419105377389237L;

	@NotEmpty(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "^([\\w\\.\\-\\_])+@(([\\w\\-\\_])+\\.)+(\\w)+$", groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String email;
	
	@ApiModelProperty("convert the existing email to OLSS contact. If this flag is true, won't update a new email, only update the existing one to OLSS contact")
	private Boolean convertToOlssContactInfo;
	
	private List<String> applyPassengerId;
	
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

	public List<String> getApplyPassengerId() {
		return applyPassengerId;
	}

	public void setApplyPassengerId(List<String> applyPassengerId) {
		this.applyPassengerId = applyPassengerId;
	}
	
}
