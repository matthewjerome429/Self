package com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class UpdateMealRequestDTO {

	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<UpdateMealRequestDetailDTO> updateMealDetails;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<UpdateMealRequestDetailDTO> getUpdateMealDetails() {
		return updateMealDetails;
	}

	public void setUpdateMealDetails(List<UpdateMealRequestDetailDTO> updateMealDetails) {
		this.updateMealDetails = updateMealDetails;
	}

}
