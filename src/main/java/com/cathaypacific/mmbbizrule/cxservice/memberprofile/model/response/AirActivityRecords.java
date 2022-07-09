package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirActivityRecords {

    private List<AirActivityRecord> airActivityRecord;

    public List<AirActivityRecord> getAirActivityRecord() {
        return airActivityRecord;
    }

    public void setAirActivityRecord(final List<AirActivityRecord> airActivityRecord) {
        this.airActivityRecord = airActivityRecord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AirActivityRecords{");
        sb.append("airActivityRecord=").append(airActivityRecord);
        sb.append('}');
        return sb.toString();
    }
}
