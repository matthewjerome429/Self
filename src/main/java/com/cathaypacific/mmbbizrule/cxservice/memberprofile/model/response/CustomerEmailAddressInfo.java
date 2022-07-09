package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerEmailAddressInfo {

    private List<CustomerEmailAddressRecord> customerEmailAddressRecord;

    public List<CustomerEmailAddressRecord> getCustomerEmailAddressRecord() {
        return customerEmailAddressRecord;
    }

    public void setCustomerEmailAddressRecord(final List<CustomerEmailAddressRecord> customerEmailAddressRecord) {
        this.customerEmailAddressRecord = customerEmailAddressRecord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerEmailAddressInfo{");
        sb.append("customerEmailAddressRecord=").append(customerEmailAddressRecord);
        sb.append('}');
        return sb.toString();
    }
}
