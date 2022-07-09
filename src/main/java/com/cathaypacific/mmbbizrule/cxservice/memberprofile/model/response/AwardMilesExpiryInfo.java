package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AwardMilesExpiryInfo {

    private String firstExpiryDate;

    private Double firstExpiryMiles;

    private String secondExpiryDate;

    private Double secondExpiryMiles;

    private String thirdExpiryDate;

    private Double thirdExpiryMiles;

    private String fourthExpiryDate;

    private Double fourthExpiryMiles;

    private String fifthExpiryDate;

    private Double fifthExpiryMiles;

    public String getFirstExpiryDate() {
        return firstExpiryDate;
    }

    public void setFirstExpiryDate(final String firstExpiryDate) {
        this.firstExpiryDate = firstExpiryDate;
    }

    public Double getFirstExpiryMiles() {
        return firstExpiryMiles;
    }

    public void setFirstExpiryMiles(final Double firstExpiryMiles) {
        this.firstExpiryMiles = firstExpiryMiles;
    }

    public String getSecondExpiryDate() {
        return secondExpiryDate;
    }

    public void setSecondExpiryDate(final String secondExpiryDate) {
        this.secondExpiryDate = secondExpiryDate;
    }

    public Double getSecondExpiryMiles() {
        return secondExpiryMiles;
    }

    public void setSecondExpiryMiles(final Double secondExpiryMiles) {
        this.secondExpiryMiles = secondExpiryMiles;
    }

    public String getThirdExpiryDate() {
        return thirdExpiryDate;
    }

    public void setThirdExpiryDate(final String thirdExpiryDate) {
        this.thirdExpiryDate = thirdExpiryDate;
    }

    public Double getThirdExpiryMiles() {
        return thirdExpiryMiles;
    }

    public void setThirdExpiryMiles(final Double thirdExpiryMiles) {
        this.thirdExpiryMiles = thirdExpiryMiles;
    }

    public String getFourthExpiryDate() {
        return fourthExpiryDate;
    }

    public void setFourthExpiryDate(final String fourthExpiryDate) {
        this.fourthExpiryDate = fourthExpiryDate;
    }

    public Double getFourthExpiryMiles() {
        return fourthExpiryMiles;
    }

    public void setFourthExpiryMiles(final Double fourthExpiryMiles) {
        this.fourthExpiryMiles = fourthExpiryMiles;
    }

    public String getFifthExpiryDate() {
        return fifthExpiryDate;
    }

    public void setFifthExpiryDate(final String fifthExpiryDate) {
        this.fifthExpiryDate = fifthExpiryDate;
    }

    public Double getFifthExpiryMiles() {
        return fifthExpiryMiles;
    }

    public void setFifthExpiryMiles(final Double fifthExpiryMiles) {
        this.fifthExpiryMiles = fifthExpiryMiles;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AwardMilesExpiryInfo{");
        sb.append("firstExpiryDate='").append(firstExpiryDate).append('\'');
        sb.append(", firstExpiryMiles='").append(firstExpiryMiles).append('\'');
        sb.append(", secondExpiryDate='").append(secondExpiryDate).append('\'');
        sb.append(", secondExpiryMiles='").append(secondExpiryMiles).append('\'');
        sb.append(", thirdExpiryDate='").append(thirdExpiryDate).append('\'');
        sb.append(", thirdExpiryMiles='").append(thirdExpiryMiles).append('\'');
        sb.append(", fourthExpiryDate='").append(fourthExpiryDate).append('\'');
        sb.append(", fourthExpiryMiles='").append(fourthExpiryMiles).append('\'');
        sb.append(", fifthExpiryDate='").append(fifthExpiryDate).append('\'');
        sb.append(", fifthExpiryMiles='").append(fifthExpiryMiles).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
