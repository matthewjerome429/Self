package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.RedemptionGroup;

public class RedemptionGroupInfo extends BaseResponse {

    private RedemptionGroup redemptionGroup;

    public RedemptionGroup getRedemptionGroup() {
        return redemptionGroup;
    }

    public void setRedemptionGroup(final RedemptionGroup redemptionGroup) {
        this.redemptionGroup = redemptionGroup;
    }
}
