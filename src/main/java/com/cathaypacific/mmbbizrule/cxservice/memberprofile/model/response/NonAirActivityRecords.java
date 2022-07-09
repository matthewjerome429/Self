package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import java.util.List;

public class NonAirActivityRecords {

    private List<NonAirActivityRecord> nonAirActivityRecord;

    public List<NonAirActivityRecord> getNonAirActivityRecord() {
        return nonAirActivityRecord;
    }

    public void setNonAirActivityRecord(final List<NonAirActivityRecord> nonAirActivityRecord) {
        this.nonAirActivityRecord = nonAirActivityRecord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NonAirActivityRecords{");
        sb.append("nonAirActivityRecord=").append(nonAirActivityRecord);
        sb.append('}');
        return sb.toString();
    }
}
