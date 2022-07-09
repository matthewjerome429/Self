package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.MemberAffiliationInfo;

public class AffiliationInfo extends BaseResponse {

    private String programCode;

    private MemberAffiliationInfo memberAffiliationInfo;

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(final String programCode) {
        this.programCode = programCode;
    }

    public MemberAffiliationInfo getMemberAffiliationInfo() {
        return memberAffiliationInfo;
    }

    public void setMemberAffiliationInfo(final MemberAffiliationInfo memberAffiliationInfo) {
        this.memberAffiliationInfo = memberAffiliationInfo;
    }

}
