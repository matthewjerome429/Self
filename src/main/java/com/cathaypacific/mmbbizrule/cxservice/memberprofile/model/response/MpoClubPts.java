package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MpoClubPts {

    private String mpoClubPtsRenewal;

    private String mpoClubPtsNextTier;

    private String mpoClubPtsBalance;

    public String getMpoClubPtsRenewal() {
        return mpoClubPtsRenewal;
    }

    public void setMpoClubPtsRenewal(final String mpoClubPtsRenewal) {
        this.mpoClubPtsRenewal = mpoClubPtsRenewal;
    }

    public String getMpoClubPtsNextTier() {
        return mpoClubPtsNextTier;
    }

    public void setMpoClubPtsNextTier(final String mpoClubPtsNextTier) {
        this.mpoClubPtsNextTier = mpoClubPtsNextTier;
    }

    public String getMpoClubPtsBalance() {
        return mpoClubPtsBalance;
    }

    public void setMpoClubPtsBalance(final String mpoClubPtsBalance) {
        this.mpoClubPtsBalance = mpoClubPtsBalance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MpoClubPts{");
        sb.append("mpoClubPtsRenewal='").append(mpoClubPtsRenewal).append('\'');
        sb.append(", mpoClubPtsNextTier='").append(mpoClubPtsNextTier).append('\'');
        sb.append(", mpoClubPtsBalance='").append(mpoClubPtsBalance).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
