package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerMobilePhoneInfo {
    private List<CustomerMobilePhoneRecord> customerMobilePhoneRecord;

    public List<CustomerMobilePhoneRecord> getCustomerMobilePhoneRecord() {
        return customerMobilePhoneRecord;
    }

    public void setCustomerMobilePhoneRecord(final List<CustomerMobilePhoneRecord> customerMobilePhoneRecord) {
        this.customerMobilePhoneRecord = customerMobilePhoneRecord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerMobilePhoneInfo{");
        sb.append("customerMobilePhoneRecord=").append(customerMobilePhoneRecord);
        sb.append('}');
        return sb.toString();
    }
}
