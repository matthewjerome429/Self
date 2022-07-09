package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PtdMpoClubPts {

    private String ptdTotalMpoClubPts;

    private String ptdBasicMpoClubPts;

    private String ptdBonusMpoClubPts;

    private String ptdMiscMpoClubPts;

    public String getPtdTotalMpoClubPts() {
        return ptdTotalMpoClubPts;
    }

    public void setPtdTotalMpoClubPts(final String ptdTotalMpoClubPts) {
        this.ptdTotalMpoClubPts = ptdTotalMpoClubPts;
    }

    public String getPtdBasicMpoClubPts() {
        return ptdBasicMpoClubPts;
    }

    public void setPtdBasicMpoClubPts(final String ptdBasicMpoClubPts) {
        this.ptdBasicMpoClubPts = ptdBasicMpoClubPts;
    }

    public String getPtdBonusMpoClubPts() {
        return ptdBonusMpoClubPts;
    }

    public void setPtdBonusMpoClubPts(final String ptdBonusMpoClubPts) {
        this.ptdBonusMpoClubPts = ptdBonusMpoClubPts;
    }

    public String getPtdMiscMpoClubPts() {
        return ptdMiscMpoClubPts;
    }

    public void setPtdMiscMpoClubPts(final String ptdMiscMpoClubPts) {
        this.ptdMiscMpoClubPts = ptdMiscMpoClubPts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PtdMpoClubPts{");
        sb.append("ptdTotalMpoClubPts='").append(ptdTotalMpoClubPts).append('\'');
        sb.append(", ptdBasicMpoClubPts='").append(ptdBasicMpoClubPts).append('\'');
        sb.append(", ptdBonusMpoClubPts='").append(ptdBonusMpoClubPts).append('\'');
        sb.append(", ptdMiscMpoClubPts='").append(ptdMiscMpoClubPts).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
