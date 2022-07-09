package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.PreferenceRecord;

public class Preference extends BaseResponse {

    private PreferenceRecord preferenceRecord;

    private Integer totalPrefenceCount;

    public PreferenceRecord getPreferenceRecord() {
        return preferenceRecord;
    }

    public void setPreferenceRecord(final PreferenceRecord preferenceRecord) {
        this.preferenceRecord = preferenceRecord;
    }

    public Integer getTotalPrefenceCount() {
        return totalPrefenceCount;
    }

    public void setTotalPrefenceCount(final Integer totalPrefenceCount) {
        this.totalPrefenceCount = totalPrefenceCount;
    }
}
