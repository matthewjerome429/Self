package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.MemberLifestyleInfo;

public class Lifestyle extends BaseResponse {

    private MemberLifestyleInfo memberLifestyleInfo;

    public MemberLifestyleInfo getMemberLifestyleInfo() {
        return memberLifestyleInfo;
    }

    public void setMemberLifestyleInfo(final MemberLifestyleInfo memberLifestyleInfo) {
        this.memberLifestyleInfo = memberLifestyleInfo;
    }
}
