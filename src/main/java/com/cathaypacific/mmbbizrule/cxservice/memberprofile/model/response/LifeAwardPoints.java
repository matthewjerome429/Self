package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LifeAwardPoints {

    private String lifeAwardTotalPoints;

    private String lifeAwardBasicPoints;

    private String lifeAwardBonusPoints;

    private String lifAwardAdjgwPoints;

    public String getLifeAwardTotalPoints() {
        return lifeAwardTotalPoints;
    }

    public void setLifeAwardTotalPoints(final String lifeAwardTotalPoints) {
        this.lifeAwardTotalPoints = lifeAwardTotalPoints;
    }

    public String getLifeAwardBasicPoints() {
        return lifeAwardBasicPoints;
    }

    public void setLifeAwardBasicPoints(final String lifeAwardBasicPoints) {
        this.lifeAwardBasicPoints = lifeAwardBasicPoints;
    }

    public String getLifeAwardBonusPoints() {
        return lifeAwardBonusPoints;
    }

    public void setLifeAwardBonusPoints(final String lifeAwardBonusPoints) {
        this.lifeAwardBonusPoints = lifeAwardBonusPoints;
    }

    public String getLifAwardAdjgwPoints() {
        return lifAwardAdjgwPoints;
    }

    public void setLifAwardAdjgwPoints(final String lifAwardAdjgwPoints) {
        this.lifAwardAdjgwPoints = lifAwardAdjgwPoints;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LifeAwardPoints{");
        sb.append("lifeAwardTotalPoints='").append(lifeAwardTotalPoints).append('\'');
        sb.append(", lifeAwardBasicPoints='").append(lifeAwardBasicPoints).append('\'');
        sb.append(", lifeAwardBonusPoints='").append(lifeAwardBonusPoints).append('\'');
        sb.append(", lifAwardAdjgwPoints='").append(lifAwardAdjgwPoints).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
