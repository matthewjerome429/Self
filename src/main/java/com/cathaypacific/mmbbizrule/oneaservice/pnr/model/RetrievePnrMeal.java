package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.List;

public class RetrievePnrMeal {
	private String mealCode;
	private String status;
	private int quantity;
	private String qulifierId;
	private String companyId;
	private List<String> freeTexts;
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
	public String getQulifierId() {
		return qulifierId;
	}
	public void setQulifierId(String qulifierId) {
		this.qulifierId = qulifierId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public List<String> getFreeTexts() {
		return freeTexts;
	}
	public void setFreeTexts(List<String> freeTexts) {
		this.freeTexts = freeTexts;
	}
    public boolean isPreSelectedMeal() {
        return preSelectedMeal;
    }
    public void setPreSelectedMeal(boolean preSelectedMeal) {
        this.preSelectedMeal = preSelectedMeal;
    }
	
}
