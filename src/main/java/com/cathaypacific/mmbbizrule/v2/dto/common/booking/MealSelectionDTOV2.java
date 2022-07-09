package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.util.List;

import com.cathaypacific.mmbbizrule.constant.MealOption;
import com.cathaypacific.mmbbizrule.model.booking.detail.PreSelectMeal;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialMeal;

public class MealSelectionDTOV2 {

	private MealOption mealOption;
	
	private boolean preSelectMealEligibility;
	
	private List<SpecialMeal> specialMealList;
	
	private List<PreSelectMeal> preSelectMealList;

	public MealOption getMealOption() {
		return mealOption;
	}

	public void setMealOption(MealOption mealOption) {
		this.mealOption = mealOption;
	}

	public List<SpecialMeal> getSpecialMealList() {
		return specialMealList;
	}

	public void setSpecialMealList(List<SpecialMeal> specialMealList) {
		this.specialMealList = specialMealList;
	}

    public boolean isPreSelectMealEligibility() {
        return preSelectMealEligibility;
    }

    public void setPreSelectMealEligibility(boolean preSelectMealEligibility) {
        this.preSelectMealEligibility = preSelectMealEligibility;
    }

    public List<PreSelectMeal> getPreSelectMealList() {
        return preSelectMealList;
    }

    public void setPreSelectMealList(List<PreSelectMeal> preSelectMealList) {
        this.preSelectMealList = preSelectMealList;
    }
	
}
