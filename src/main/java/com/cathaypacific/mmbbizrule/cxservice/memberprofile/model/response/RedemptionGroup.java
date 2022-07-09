package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedemptionGroup {

    private List<NomineeRecord> nomineeRecord;

    public List<NomineeRecord> getNomineeRecord() {
        return nomineeRecord;
    }

    public void setNomineeRecord(final List<NomineeRecord> nomineeRecord) {
        this.nomineeRecord = nomineeRecord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RedemptionGroup{");
        sb.append("nomineeRecord=").append(nomineeRecord);
        sb.append('}');
        return sb.toString();
    }
}
