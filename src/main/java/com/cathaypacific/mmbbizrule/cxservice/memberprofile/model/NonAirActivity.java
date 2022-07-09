package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.NonAirActivityRecords;

public class NonAirActivity extends BaseResponse {
    private NonAirActivityRecords nonAirActivityRecords;

    public NonAirActivityRecords getNonAirActivityRecords() {
        return nonAirActivityRecords;
    }

    public void setNonAirActivityRecords(final NonAirActivityRecords nonAirActivityRecords) {
        this.nonAirActivityRecords = nonAirActivityRecords;
    }
}
