package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class MealDetailResponse extends BaseResponse {

    private String jobId;

    private List<MealDetail> mealDetails;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<MealDetail> getMealDetails() {
        return mealDetails;
    }

    public void setMealDetails(List<MealDetail> mealDetails) {
        this.mealDetails = mealDetails;
    }

    public void addMealDetails(MealDetail mealDetail) {
        if (CollectionUtils.isEmpty(this.mealDetails)) {
            this.mealDetails = new ArrayList<>();
        }
        this.mealDetails.add(mealDetail);
    }
}
