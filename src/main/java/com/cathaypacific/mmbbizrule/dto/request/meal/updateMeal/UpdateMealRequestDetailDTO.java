package com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealRequestDetailDTO;

public class UpdateMealRequestDetailDTO extends MealSegmentInfoDTO{

	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<MealRequestDetailDTO> mealDetails;
	
	private String companyId;
	private String operator;
	
	public List<MealRequestDetailDTO> getMealDetails() {
		return mealDetails;
	}

	public void setMealDetails(List<MealRequestDetailDTO> mealDetails) {
		this.mealDetails = mealDetails;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}


	
}
