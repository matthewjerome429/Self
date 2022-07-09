package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class MealDescriptionResponse extends BaseResponse {

    private String jobId;

    private List<MealDescription> mealDescriptions;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<MealDescription> getMealDescriptions() {
        return mealDescriptions;
    }

    public void setMealDescriptions(List<MealDescription> mealDescriptions) {
        this.mealDescriptions = mealDescriptions;
    }

    public void addMealDescription(MealDescription mealDesctiption) {
        if (CollectionUtils.isEmpty(this.mealDescriptions)) {
            this.mealDescriptions = new ArrayList<>();
        }
        this.mealDescriptions.add(mealDesctiption);
    }
}
