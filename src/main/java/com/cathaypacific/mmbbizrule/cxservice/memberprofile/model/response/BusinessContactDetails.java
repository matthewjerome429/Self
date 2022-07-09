package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessContactDetails {

    private String companyName;

    private String businessTitle;

    private String businessAddressLine1;

    private String businessAddressLine2;

    private String businessAddressLine3;

    private String businessAddressCity;

    private String businessAddressState;

    private String businessAddressCountryName;

    private String businessAddressCountryCode;

    private String businessAddressPostcode;

    private String businessPhone;

    private String businessAddressInvalidInd;

    private String businessPhoneInvalidInd;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessTitle() {
        return businessTitle;
    }

    public void setBusinessTitle(final String businessTitle) {
        this.businessTitle = businessTitle;
    }

    public String getBusinessAddressLine1() {
        return businessAddressLine1;
    }

    public void setBusinessAddressLine1(final String businessAddressLine1) {
        this.businessAddressLine1 = businessAddressLine1;
    }

    public String getBusinessAddressLine2() {
        return businessAddressLine2;
    }

    public void setBusinessAddressLine2(final String businessAddressLine2) {
        this.businessAddressLine2 = businessAddressLine2;
    }

    public String getBusinessAddressLine3() {
        return businessAddressLine3;
    }

    public void setBusinessAddressLine3(final String businessAddressLine3) {
        this.businessAddressLine3 = businessAddressLine3;
    }

    public String getBusinessAddressCity() {
        return businessAddressCity;
    }

    public void setBusinessAddressCity(final String businessAddressCity) {
        this.businessAddressCity = businessAddressCity;
    }

    public String getBusinessAddressState() {
        return businessAddressState;
    }

    public void setBusinessAddressState(final String businessAddressState) {
        this.businessAddressState = businessAddressState;
    }

    public String getBusinessAddressCountryName() {
        return businessAddressCountryName;
    }

    public void setBusinessAddressCountryName(final String businessAddressCountryName) {
        this.businessAddressCountryName = businessAddressCountryName;
    }

    public String getBusinessAddressCountryCode() {
        return businessAddressCountryCode;
    }

    public void setBusinessAddressCountryCode(final String businessAddressCountryCode) {
        this.businessAddressCountryCode = businessAddressCountryCode;
    }

    public String getBusinessAddressPostcode() {
        return businessAddressPostcode;
    }

    public void setBusinessAddressPostcode(final String businessAddressPostcode) {
        this.businessAddressPostcode = businessAddressPostcode;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(final String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getBusinessAddressInvalidInd() {
        return businessAddressInvalidInd;
    }

    public void setBusinessAddressInvalidInd(final String businessAddressInvalidInd) {
        this.businessAddressInvalidInd = businessAddressInvalidInd;
    }

    public String getBusinessPhoneInvalidInd() {
        return businessPhoneInvalidInd;
    }

    public void setBusinessPhoneInvalidInd(final String businessPhoneInvalidInd) {
        this.businessPhoneInvalidInd = businessPhoneInvalidInd;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BusinessContactDetails{");
        sb.append("companyName='").append(companyName).append('\'');
        sb.append(", businessTitle='").append(businessTitle).append('\'');
        sb.append(", businessAddressLine1='").append(businessAddressLine1).append('\'');
        sb.append(", businessAddressLine2='").append(businessAddressLine2).append('\'');
        sb.append(", businessAddressLine3='").append(businessAddressLine3).append('\'');
        sb.append(", businessAddressCity='").append(businessAddressCity).append('\'');
        sb.append(", businessAddressState='").append(businessAddressState).append('\'');
        sb.append(", businessAddressCountryName='").append(businessAddressCountryName).append('\'');
        sb.append(", businessAddressCountryCode='").append(businessAddressCountryCode).append('\'');
        sb.append(", businessAddressPostcode='").append(businessAddressPostcode).append('\'');
        sb.append(", businessPhone='").append(businessPhone).append('\'');
        sb.append(", businessAddressInvalidInd='").append(businessAddressInvalidInd).append('\'');
        sb.append(", businessPhoneInvalidInd='").append(businessPhoneInvalidInd).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
