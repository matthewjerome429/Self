package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CoBrandCardRecord;

public class CoBrandCard extends BaseResponse {

    private List<CoBrandCardRecord> coBrandCard;

    private String enquiryType;

    public List<CoBrandCardRecord> getCoBrandCard() {
        return coBrandCard;
    }

    public void setCoBrandCard(final List<CoBrandCardRecord> coBrandCard) {
        this.coBrandCard = coBrandCard;
    }

    public String getEnquiryType() {
        return enquiryType;
    }

    public void setEnquiryType(final String enquiryType) {
        this.enquiryType = enquiryType;
    }
}
