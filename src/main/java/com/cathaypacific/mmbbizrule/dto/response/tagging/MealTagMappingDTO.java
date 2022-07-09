package com.cathaypacific.mmbbizrule.dto.response.tagging;

import java.io.Serializable;

public class MealTagMappingDTO implements Serializable{

	private static final long serialVersionUID = 4889382003606770550L;
	
	private String mealType;
	private String taggingName;
	
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public String getTaggingName() {
		return taggingName;
	}
	public void setTaggingName(String taggingName) {
		this.taggingName = taggingName;
	}
	
	
}
