package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeriodToDateAwardPoints {

    private String ptdAwardTotalPoints;

    private String ptdAwardBasicPoints;

    private String ptdAwardBonusPoints;

    private String ptdAwardAdjgwPoints;

    public String getPtdAwardTotalPoints() {
        return ptdAwardTotalPoints;
    }

    public void setPtdAwardTotalPoints(final String ptdAwardTotalPoints) {
        this.ptdAwardTotalPoints = ptdAwardTotalPoints;
    }

    public String getPtdAwardBasicPoints() {
        return ptdAwardBasicPoints;
    }

    public void setPtdAwardBasicPoints(final String ptdAwardBasicPoints) {
        this.ptdAwardBasicPoints = ptdAwardBasicPoints;
    }

    public String getPtdAwardBonusPoints() {
        return ptdAwardBonusPoints;
    }

    public void setPtdAwardBonusPoints(final String ptdAwardBonusPoints) {
        this.ptdAwardBonusPoints = ptdAwardBonusPoints;
    }

    public String getPtdAwardAdjgwPoints() {
        return ptdAwardAdjgwPoints;
    }

    public void setPtdAwardAdjgwPoints(final String ptdAwardAdjgwPoints) {
        this.ptdAwardAdjgwPoints = ptdAwardAdjgwPoints;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PeriodToDateAwardPoints{");
        sb.append("ptdAwardTotalPoints='").append(ptdAwardTotalPoints).append('\'');
        sb.append(", ptdAwardBasicPoints='").append(ptdAwardBasicPoints).append('\'');
        sb.append(", ptdAwardBonusPoints='").append(ptdAwardBonusPoints).append('\'');
        sb.append(", ptdAwardAdjgwPoints='").append(ptdAwardAdjgwPoints).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
