package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class Courses {

    private String course;

    private List<MealDetails> mealDetails;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public List<MealDetails> getMealDetails() {
        return mealDetails;
    }

    public void setMealDetails(List<MealDetails> mealDetails) {
        this.mealDetails = mealDetails;
    }

    public void addMealDetails(MealDetails mealDetails) {
        if (CollectionUtils.isEmpty(this.mealDetails)) {
            this.mealDetails = new ArrayList<>();
        }
        this.mealDetails.add(mealDetails);
    }
}
