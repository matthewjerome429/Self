package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerFaxInfo {

    private List<CustomerFaxRecord> customerFaxRecord;

    public List<CustomerFaxRecord> getCustomerFaxRecord() {
        return customerFaxRecord;
    }

    public void setCustomerFaxRecord(final List<CustomerFaxRecord> customerFaxRecord) {
        this.customerFaxRecord = customerFaxRecord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerFaxInfo{");
        sb.append("customerFaxRecord=").append(customerFaxRecord);
        sb.append('}');
        return sb.toString();
    }
}
