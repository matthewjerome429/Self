package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.LocalLanguageCustomerInfo;

public class LocalLangInfo extends BaseResponse {

    private LocalLanguageCustomerInfo localLanguageCustomerInfo;

    public LocalLanguageCustomerInfo getLocalLanguageCustomerInfo() {
        return localLanguageCustomerInfo;
    }

    public void setLocalLanguageCustomerInfo(final LocalLanguageCustomerInfo localLanguageCustomerInfo) {
        this.localLanguageCustomerInfo = localLanguageCustomerInfo;
    }
}
