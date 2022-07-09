package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private String totalMilesAvailable;

    private MpoClubPts mpoClubPts;

    public String getTotalMilesAvailable() {
        return totalMilesAvailable;
    }

    public void setTotalMilesAvailable(final String totalMilesAvailable) {
        this.totalMilesAvailable = totalMilesAvailable;
    }

    public MpoClubPts getMpoClubPts() {
        return mpoClubPts;
    }

    public void setMpoClubPts(final MpoClubPts mpoClubPts) {
        this.mpoClubPts = mpoClubPts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("totalMilesAvailable='").append(totalMilesAvailable).append('\'');
        sb.append(", mpoClubPts=").append(mpoClubPts);
        sb.append('}');
        return sb.toString();
    }
}
