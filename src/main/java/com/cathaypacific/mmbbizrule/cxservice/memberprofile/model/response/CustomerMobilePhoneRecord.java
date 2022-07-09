package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerMobilePhoneRecord {

    private String mobileType;

    private String mobilePhoneNumber;

    private String wapUserIndicator;

    private String mobileMessagePreference;

    private String invalidIndicator;

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(final String mobileType) {
        this.mobileType = mobileType;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(final String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getWapUserIndicator() {
        return wapUserIndicator;
    }

    public void setWapUserIndicator(final String wapUserIndicator) {
        this.wapUserIndicator = wapUserIndicator;
    }

    public String getMobileMessagePreference() {
        return mobileMessagePreference;
    }

    public void setMobileMessagePreference(final String mobileMessagePreference) {
        this.mobileMessagePreference = mobileMessagePreference;
    }

    public String getInvalidIndicator() {
        return invalidIndicator;
    }

    public void setInvalidIndicator(final String invalidIndicator) {
        this.invalidIndicator = invalidIndicator;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerMobilePhoneRecord{");
        sb.append("mobileType='").append(mobileType).append('\'');
        sb.append(", mobilePhoneNumber='").append(mobilePhoneNumber).append('\'');
        sb.append(", wapUserIndicator='").append(wapUserIndicator).append('\'');
        sb.append(", mobileMessagePreference='").append(mobileMessagePreference).append('\'');
        sb.append(", invalidIndicator='").append(invalidIndicator).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
