package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeContactDetails {

    private String homeAddressLine1;

    private String homeAddressLine2;

    private String homeAddressLine3;

    private String homeAddressCity;

    private String homeAddressState;

    private String homeAddressCountryName;

    private String homeAddressCountryCode;

    private String homeAddressPostcode;

    private String homePhone;

    private String homeAddressInvalidInd;

    private String homePhoneInvalidInd;

    public String getHomeAddressLine1() {
        return homeAddressLine1;
    }

    public void setHomeAddressLine1(final String homeAddressLine1) {
        this.homeAddressLine1 = homeAddressLine1;
    }

    public String getHomeAddressLine2() {
        return homeAddressLine2;
    }

    public void setHomeAddressLine2(final String homeAddressLine2) {
        this.homeAddressLine2 = homeAddressLine2;
    }

    public String getHomeAddressLine3() {
        return homeAddressLine3;
    }

    public void setHomeAddressLine3(final String homeAddressLine3) {
        this.homeAddressLine3 = homeAddressLine3;
    }

    public String getHomeAddressCity() {
        return homeAddressCity;
    }

    public void setHomeAddressCity(final String homeAddressCity) {
        this.homeAddressCity = homeAddressCity;
    }

    public String getHomeAddressState() {
        return homeAddressState;
    }

    public void setHomeAddressState(final String homeAddressState) {
        this.homeAddressState = homeAddressState;
    }

    public String getHomeAddressCountryName() {
        return homeAddressCountryName;
    }

    public void setHomeAddressCountryName(final String homeAddressCountryName) {
        this.homeAddressCountryName = homeAddressCountryName;
    }

    public String getHomeAddressCountryCode() {
        return homeAddressCountryCode;
    }

    public void setHomeAddressCountryCode(final String homeAddressCountryCode) {
        this.homeAddressCountryCode = homeAddressCountryCode;
    }

    public String getHomeAddressPostcode() {
        return homeAddressPostcode;
    }

    public void setHomeAddressPostcode(final String homeAddressPostcode) {
        this.homeAddressPostcode = homeAddressPostcode;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(final String homePhone) {
        this.homePhone = homePhone;
    }

    public String getHomeAddressInvalidInd() {
        return homeAddressInvalidInd;
    }

    public void setHomeAddressInvalidInd(final String homeAddressInvalidInd) {
        this.homeAddressInvalidInd = homeAddressInvalidInd;
    }

    public String getHomePhoneInvalidInd() {
        return homePhoneInvalidInd;
    }

    public void setHomePhoneInvalidInd(final String homePhoneInvalidInd) {
        this.homePhoneInvalidInd = homePhoneInvalidInd;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HomeContactDetails{");
        sb.append("homeAddressLine1='").append(homeAddressLine1).append('\'');
        sb.append(", homeAddressLine2='").append(homeAddressLine2).append('\'');
        sb.append(", homeAddressLine3='").append(homeAddressLine3).append('\'');
        sb.append(", homeAddressCity='").append(homeAddressCity).append('\'');
        sb.append(", homeAddressState='").append(homeAddressState).append('\'');
        sb.append(", homeAddressCountryName='").append(homeAddressCountryName).append('\'');
        sb.append(", homeAddressCountryCode='").append(homeAddressCountryCode).append('\'');
        sb.append(", homeAddressPostcode='").append(homeAddressPostcode).append('\'');
        sb.append(", homePhone='").append(homePhone).append('\'');
        sb.append(", homeAddressInvalidInd='").append(homeAddressInvalidInd).append('\'');
        sb.append(", homePhoneInvalidInd='").append(homePhoneInvalidInd).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
