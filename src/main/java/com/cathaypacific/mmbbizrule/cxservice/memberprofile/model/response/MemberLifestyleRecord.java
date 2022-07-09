package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberLifestyleRecord {

    private String lifestyleCategoryCode;

    private String lifestyleCode;

    private String lifestyleDescription;

    public String getLifestyleCategoryCode() {
        return lifestyleCategoryCode;
    }

    public void setLifestyleCategoryCode(final String lifestyleCategoryCode) {
        this.lifestyleCategoryCode = lifestyleCategoryCode;
    }

    public String getLifestyleCode() {
        return lifestyleCode;
    }

    public void setLifestyleCode(final String lifestyleCode) {
        this.lifestyleCode = lifestyleCode;
    }

    public String getLifestyleDescription() {
        return lifestyleDescription;
    }

    public void setLifestyleDescription(final String lifestyleDescription) {
        this.lifestyleDescription = lifestyleDescription;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MemberLifestyleRecord{");
        sb.append("lifestyleCategoryCode='").append(lifestyleCategoryCode).append('\'');
        sb.append(", lifestyleCode='").append(lifestyleCode).append('\'');
        sb.append(", lifestyleDescription='").append(lifestyleDescription).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
