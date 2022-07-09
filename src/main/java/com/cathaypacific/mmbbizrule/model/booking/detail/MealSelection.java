package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.util.List;

import com.cathaypacific.mmbbizrule.constant.MealOption;

public class MealSelection {

	private MealOption mealOption;
	
	private boolean preSelectMealEligibility;
	
	private List<SpecialMeal> specialMeals;
    
    private List<PreSelectMeal> preSelectMeals;

	public MealOption getMealOption() {
		return mealOption;
	}

	public void setMealOption(MealOption mealOption) {
		this.mealOption = mealOption;
	}

	public List<SpecialMeal> getSpecialMeals() {
		return specialMeals;
	}

	public void setSpecialMeals(List<SpecialMeal> specialMeals) {
		this.specialMeals = specialMeals;
	}

    public boolean isPreSelectMealEligibility() {
        return preSelectMealEligibility;
    }

    public void setPreSelectMealEligibility(boolean preSelectMealEligibility) {
        this.preSelectMealEligibility = preSelectMealEligibility;
    }

    public List<PreSelectMeal> getPreSelectMeals() {
        return preSelectMeals;
    }

    public void setPreSelectMeals(List<PreSelectMeal> preSelectMeals) {
        this.preSelectMeals = preSelectMeals;
    }
	
}
