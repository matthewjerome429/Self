package com.cathaypacific.mmbbizrule.dto.request.meal.cancelMeal;

import java.util.List;

import com.cathaypacific.mmbbizrule.dto.request.meal.MealSegmentInfoDTO;

public class CancelMealDetailDTO extends MealSegmentInfoDTO{

	private List<String> paxIds;
	
	public List<String> getPaxIds() {
		return paxIds;
	}

	public void setPaxIds(List<String> paxIds) {
		this.paxIds = paxIds;
	}

}
