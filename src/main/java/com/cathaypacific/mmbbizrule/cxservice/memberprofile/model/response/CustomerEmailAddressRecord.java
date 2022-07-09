package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerEmailAddressRecord {

    private String emailAddressType;

    private String emailAddress;

    private String preferredEmailIndicator;

    private String emailMessagePreference;

    private String emailFormatType;

    private String invalidIndicator;

    public String getEmailAddressType() {
        return emailAddressType;
    }

    public void setEmailAddressType(final String emailAddressType) {
        this.emailAddressType = emailAddressType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPreferredEmailIndicator() {
        return preferredEmailIndicator;
    }

    public void setPreferredEmailIndicator(final String preferredEmailIndicator) {
        this.preferredEmailIndicator = preferredEmailIndicator;
    }

    public String getEmailMessagePreference() {
        return emailMessagePreference;
    }

    public void setEmailMessagePreference(final String emailMessagePreference) {
        this.emailMessagePreference = emailMessagePreference;
    }

    public String getEmailFormatType() {
        return emailFormatType;
    }

    public void setEmailFormatType(final String emailFormatType) {
        this.emailFormatType = emailFormatType;
    }

    public String getInvalidIndicator() {
        return invalidIndicator;
    }

    public void setInvalidIndicator(final String invalidIndicator) {
        this.invalidIndicator = invalidIndicator;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerEmailAddressRecord{");
        sb.append("emailAddressType='").append(emailAddressType).append('\'');
        sb.append(", emailAddress='").append(emailAddress).append('\'');
        sb.append(", preferredEmailIndicator='").append(preferredEmailIndicator).append('\'');
        sb.append(", emailMessagePreference='").append(emailMessagePreference).append('\'');
        sb.append(", emailFormatType='").append(emailFormatType).append('\'');
        sb.append(", invalidIndicator='").append(invalidIndicator).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
