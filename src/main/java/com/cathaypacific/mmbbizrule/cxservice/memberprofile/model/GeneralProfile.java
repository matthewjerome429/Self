package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerEmailAddressInfo;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerMobilePhoneInfo;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerRecord;

public class GeneralProfile extends BaseResponse {

    private CustomerRecord customerRecord;

    private CustomerEmailAddressInfo customerEmailAddressInfo;

    private CustomerMobilePhoneInfo customerMobilePhoneInfo;

    public CustomerRecord getCustomerRecord() {
        return customerRecord;
    }

    public void setCustomerRecord(final CustomerRecord customerRecord) {
        this.customerRecord = customerRecord;
    }

    public CustomerEmailAddressInfo getCustomerEmailAddressInfo() {
        return customerEmailAddressInfo;
    }

    public void setCustomerEmailAddressInfo(final CustomerEmailAddressInfo customerEmailAddressInfo) {
        this.customerEmailAddressInfo = customerEmailAddressInfo;
    }

    public CustomerMobilePhoneInfo getCustomerMobilePhoneInfo() {
        return customerMobilePhoneInfo;
    }

    public void setCustomerMobilePhoneInfo(final CustomerMobilePhoneInfo customerMobilePhoneInfo) {
        this.customerMobilePhoneInfo = customerMobilePhoneInfo;
    }
}
