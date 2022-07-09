package com.cathaypacific.mmbbizrule.dto.request.meal;

import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MealRequestDetailDTO {
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String passengerId;
	
	private String mealCode;
	private int quantity;
	
	@JsonIgnore
	private String freeText;
	
	private boolean preSelectedMeal;
	
	public String getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}
	public String getMealCode() {
		return mealCode;
	}
	public void setMealCode(String mealCode) {
		this.mealCode = mealCode;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getFreeText() {
		return freeText;
	}
	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}
    public boolean isPreSelectedMeal() {
        return preSelectedMeal;
    }
    public void setPreSelectedMeal(boolean preSelectedMeal) {
        this.preSelectedMeal = preSelectedMeal;
    }
	
}
