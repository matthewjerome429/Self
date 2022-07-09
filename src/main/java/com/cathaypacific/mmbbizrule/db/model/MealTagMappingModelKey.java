package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class MealTagMappingModelKey implements Serializable{
	
	private static final long serialVersionUID = 2636198689978108484L;

    @NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
    
    @NotNull
    @Column(name = "meal_type")
    private String mealType;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
    
    

}
