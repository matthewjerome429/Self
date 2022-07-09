package com.cathaypacific.mmbbizrule.dto.request.oneaoperation;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class AddRmRequestDTO {
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<AddRmDetail> addRmDetails;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<AddRmDetail> getAddRmDetails() {
		return addRmDetails;
	}

	public void setAddRmDetails(List<AddRmDetail> addRmDetails) {
		this.addRmDetails = addRmDetails;
	}
	
}
