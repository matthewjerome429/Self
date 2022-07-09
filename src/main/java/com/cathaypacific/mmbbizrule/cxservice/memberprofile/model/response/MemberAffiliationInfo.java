package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberAffiliationInfo {

    private List<MemberAffiliationRecord> memberAffiliationRecord;

    public List<MemberAffiliationRecord> getMemberAffiliationRecord() {
        return memberAffiliationRecord;
    }

    public void setMemberAffiliationRecord(final List<MemberAffiliationRecord> memberAffiliationRecord) {
        this.memberAffiliationRecord = memberAffiliationRecord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MemberAffiliationInfo{");
        sb.append("memberAffiliationRecord=").append(memberAffiliationRecord);
        sb.append('}');
        return sb.toString();
    }
}
