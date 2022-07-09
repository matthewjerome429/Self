package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;

public class Eligibility implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6215229977845396488L;
    // Eligbility for One Click Check-in. 1 = true, 0 = false
    private Integer OCCI;
    // Eligbility for Meal Pre-select. 1 = true, 0 = false
    private Integer mealPreSelect;

    public Integer getOCCI() {
        return OCCI;
    }

    public void setOCCI(Integer oCCI) {
        OCCI = oCCI;
    }

    public Integer getMealPreSelect() {
        return mealPreSelect;
    }

    public void setMealPreSelect(Integer mealPreSelect) {
        this.mealPreSelect = mealPreSelect;
    }

}
