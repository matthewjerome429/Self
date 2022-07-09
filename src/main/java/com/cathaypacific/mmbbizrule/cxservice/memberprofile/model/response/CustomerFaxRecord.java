package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerFaxRecord {

    private String faxType;

    private String faxNumber;

    private String invalidIndicator;

    public String getFaxType() {
        return faxType;
    }

    public void setFaxType(final String faxType) {
        this.faxType = faxType;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(final String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getInvalidIndicator() {
        return invalidIndicator;
    }

    public void setInvalidIndicator(final String invalidIndicator) {
        this.invalidIndicator = invalidIndicator;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerFaxRecord{");
        sb.append("faxType='").append(faxType).append('\'');
        sb.append(", faxNumber='").append(faxNumber).append('\'');
        sb.append(", invalidIndicator='").append(invalidIndicator).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
