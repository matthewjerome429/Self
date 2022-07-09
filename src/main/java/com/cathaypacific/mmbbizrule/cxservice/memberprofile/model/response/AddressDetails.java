package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDetails {

    private String mailingAddressIndicator;

    private HomeContactDetails homeContactDetails;

    private BusinessContactDetails businessContactDetails;

    private String preferredLanguageMailingInd;

    public String getMailingAddressIndicator() {
        return mailingAddressIndicator;
    }

    public void setMailingAddressIndicator(final String mailingAddressIndicator) {
        this.mailingAddressIndicator = mailingAddressIndicator;
    }

    public HomeContactDetails getHomeContactDetails() {
        return homeContactDetails;
    }

    public void setHomeContactDetails(final HomeContactDetails homeContactDetails) {
        this.homeContactDetails = homeContactDetails;
    }

    public BusinessContactDetails getBusinessContactDetails() {
        return businessContactDetails;
    }

    public void setBusinessContactDetails(final BusinessContactDetails businessContactDetails) {
        this.businessContactDetails = businessContactDetails;
    }

    public String getPreferredLanguageMailingInd() {
        return preferredLanguageMailingInd;
    }

    public void setPreferredLanguageMailingInd(final String preferredLanguageMailingInd) {
        this.preferredLanguageMailingInd = preferredLanguageMailingInd;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddressDetails{");
        sb.append("mailingAddressIndicator='").append(mailingAddressIndicator).append('\'');
        sb.append(", homeContactDetails=").append(homeContactDetails);
        sb.append(", businessContactDetails=").append(businessContactDetails);
        sb.append(", preferredLanguageMailingInd='").append(preferredLanguageMailingInd).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
