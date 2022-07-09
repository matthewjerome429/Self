package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.AddressDetails;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerFaxInfo;

public class ContactInfo extends BaseResponse {

    private AddressDetails addressDetails;

    private CustomerFaxInfo customerFaxInfo;

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(final AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public CustomerFaxInfo getCustomerFaxInfo() {
        return customerFaxInfo;
    }

    public void setCustomerFaxInfo(final CustomerFaxInfo customerFaxInfo) {
        this.customerFaxInfo = customerFaxInfo;
    }
}
