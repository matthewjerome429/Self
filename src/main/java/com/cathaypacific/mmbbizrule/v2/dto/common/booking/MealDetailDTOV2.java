package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.DishName;

public class MealDetailDTOV2 {
	private String mealCode;
	private String status;
	private int quantity;
	private String companyId;
	private List<String> tag;
	private DishName dishName;
    private boolean preSelectedMeal;
	
	public String getMealCode() {
		return mealCode;
	}
	public void setMealCode(String mealCode) {
		this.mealCode = mealCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public DishName getDishName() {
        return dishName;
    }

    public void setDishName(DishName dishName) {
        this.dishName = dishName;
    }

    public boolean isPreSelectedMeal() {
        return preSelectedMeal;
    }

    public void setPreSelectedMeal(boolean preSelectedMeal) {
        this.preSelectedMeal = preSelectedMeal;
    }

}
