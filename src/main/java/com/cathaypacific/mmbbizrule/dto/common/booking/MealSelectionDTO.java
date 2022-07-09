package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.util.List;

import com.cathaypacific.mmbbizrule.constant.MealOption;

public class MealSelectionDTO {

	private MealOption mealOption;
	
	private List<String> specialMealList;

	public MealOption getMealOption() {
		return mealOption;
	}

	public void setMealOption(MealOption mealOption) {
		this.mealOption = mealOption;
	}

	public List<String> getSpecialMealList() {
		return specialMealList;
	}

	public void setSpecialMealList(List<String> specialMealList) {
		this.specialMealList = specialMealList;
	}
}
