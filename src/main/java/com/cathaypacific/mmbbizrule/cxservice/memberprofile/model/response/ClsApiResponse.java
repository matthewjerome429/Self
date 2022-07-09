package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClsApiResponse {

    private String statusCode;

    private Profile profile;

    // 11Preference <- due to CLS team bad interface design
    private PreferenceRecord preferenceRecord;

    private Integer totalPrefenceCount;

    // 12CoBrand
    private List<CoBrandCardRecord> card;

    private String enquiryType;

    // 13Promotion
    public List<RedemptionRow> redemptionRow;

    public List<TargetRow> targetRow;

    private List<ClsApiError> apiErrors;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(final Profile profile) {
        this.profile = profile;
    }

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

    public List<CoBrandCardRecord> getCard() {
        return card;
    }

    public void setCard(final List<CoBrandCardRecord> card) {
        this.card = card;
    }

    public String getEnquiryType() {
        return enquiryType;
    }

    public void setEnquiryType(final String enquiryType) {
        this.enquiryType = enquiryType;
    }

    public List<RedemptionRow> getRedemptionRow() {
        return redemptionRow;
    }

    public void setRedemptionRow(final List<RedemptionRow> redemptionRow) {
        this.redemptionRow = redemptionRow;
    }

    public List<TargetRow> getTargetRow() {
        return targetRow;
    }

    public void setTargetRow(final List<TargetRow> targetRow) {
        this.targetRow = targetRow;
    }

    public List<ClsApiError> getApiErrors() {
        return apiErrors;
    }

    public void setApiErrors(final List<ClsApiError> apiErrors) {
        this.apiErrors = apiErrors;
    }
}