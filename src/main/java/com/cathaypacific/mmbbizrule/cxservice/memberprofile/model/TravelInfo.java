package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.AirActivityRecords;

public class TravelInfo extends BaseResponse {
    private AirActivityRecords airActivityRecords;

    public AirActivityRecords getAirActivityRecords() {
        return airActivityRecords;
    }

    public void setAirActivityRecords(final AirActivityRecords airActivityRecords) {
        this.airActivityRecords = airActivityRecords;
    }
}
