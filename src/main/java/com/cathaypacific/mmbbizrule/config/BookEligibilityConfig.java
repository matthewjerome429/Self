package com.cathaypacific.mmbbizrule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookEligibilityConfig {

    @Value("${enable.preselected.meal}")
    private boolean enablePreselectedMeal;
    @Value("${olss.book.eligibility.pnreligibility.endpoint}")
    private String pnrEligibilityUrl;
    @Value("${olss.book.eligibility.mealdescription.endpoint}")
    private String mealdescriptionUrl;
    @Value("${olss.book.eligibility.mealdetails.endpoint}")
    private String mealdetailsUrl;
    @Value("${olss.book.eligibility.facade.apikey}")
    private String facadeApikey;

    public String getPnrEligibilityUrl() {
        return pnrEligibilityUrl;
    }

    public void setPnrEligibilityUrl(String pnrEligibilityUrl) {
        this.pnrEligibilityUrl = pnrEligibilityUrl;
    }

    public String getMealdescriptionUrl() {
        return mealdescriptionUrl;
    }

    public void setMealdescriptionUrl(String mealdescriptionUrl) {
        this.mealdescriptionUrl = mealdescriptionUrl;
    }

    public String getMealdetailsUrl() {
        return mealdetailsUrl;
    }

    public void setMealdetailsUrl(String mealdetailsUrl) {
        this.mealdetailsUrl = mealdetailsUrl;
    }

    public boolean isEnablePreselectedMeal() {
        return enablePreselectedMeal;
    }

    public void setEnablePreselectedMeal(boolean enablePreselectedMeal) {
        this.enablePreselectedMeal = enablePreselectedMeal;
    }

    public String getFacadeApikey() {
        return facadeApikey;
    }

    public void setFacadeApikey(String facadeApikey) {
        this.facadeApikey = facadeApikey;
    }

}
