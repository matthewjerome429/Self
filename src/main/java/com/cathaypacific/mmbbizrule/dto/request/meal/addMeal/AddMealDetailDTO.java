package com.cathaypacific.mmbbizrule.dto.request.meal.addMeal;

import java.util.List;

import com.cathaypacific.mmbbizrule.dto.request.meal.MealSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealRequestDetailDTO;

public class AddMealDetailDTO extends MealSegmentInfoDTO{

	private List<MealRequestDetailDTO> mealRequestDetailDTOs;
	
	private String companyId;

	public List<MealRequestDetailDTO> getPaxMealDetails() {
		return mealRequestDetailDTOs;
	}

	public void setPaxMealDetails(List<MealRequestDetailDTO> paxMealDetails) {
		this.mealRequestDetailDTOs = paxMealDetails;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
}
