package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LifeMpoClubPts {

    private String lifeTotalMpoClubPts;

    private String lifeBasicMpoClubPts;

    private String lifeBonusMpoClubPts;

    private String lifeMiscMpoClubPts;

    public String getLifeTotalMpoClubPts() {
        return lifeTotalMpoClubPts;
    }

    public void setLifeTotalMpoClubPts(final String lifeTotalMpoClubPts) {
        this.lifeTotalMpoClubPts = lifeTotalMpoClubPts;
    }

    public String getLifeBasicMpoClubPts() {
        return lifeBasicMpoClubPts;
    }

    public void setLifeBasicMpoClubPts(final String lifeBasicMpoClubPts) {
        this.lifeBasicMpoClubPts = lifeBasicMpoClubPts;
    }

    public String getLifeBonusMpoClubPts() {
        return lifeBonusMpoClubPts;
    }

    public void setLifeBonusMpoClubPts(final String lifeBonusMpoClubPts) {
        this.lifeBonusMpoClubPts = lifeBonusMpoClubPts;
    }

    public String getLifeMiscMpoClubPts() {
        return lifeMiscMpoClubPts;
    }

    public void setLifeMiscMpoClubPts(final String lifeMiscMpoClubPts) {
        this.lifeMiscMpoClubPts = lifeMiscMpoClubPts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LifeMpoClubPts{");
        sb.append("lifeTotalMpoClubPts='").append(lifeTotalMpoClubPts).append('\'');
        sb.append(", lifeBasicMpoClubPts='").append(lifeBasicMpoClubPts).append('\'');
        sb.append(", lifeBonusMpoClubPts='").append(lifeBonusMpoClubPts).append('\'');
        sb.append(", lifeMiscMpoClubPts='").append(lifeMiscMpoClubPts).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
