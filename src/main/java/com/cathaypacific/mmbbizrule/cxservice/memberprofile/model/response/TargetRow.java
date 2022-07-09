package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetRow {

    private String targetCode;

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(final String targetCode) {
        this.targetCode = targetCode;
    }
}
