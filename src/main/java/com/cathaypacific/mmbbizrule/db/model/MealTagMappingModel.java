package com.cathaypacific.mmbbizrule.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tb_meal_tag_mapping")
@IdClass(MealTagMappingModelKey.class)
public class MealTagMappingModel {

	@Id
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
	
    @Id
    @NotNull
    @Column(name = "meal_type")
    private String mealType;

    @NotNull
    @Column(name = "tagging_name")
    private String taggingName;

    
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

	public String getTaggingName() {
		return taggingName;
	}

	public void setTaggingName(String taggingName) {
		this.taggingName = taggingName;
	}
    
}
