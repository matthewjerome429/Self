package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberLifestyleInfo {

    private List<MemberLifestyleRecord> memberLifestyleRecord;

    public List<MemberLifestyleRecord> getMemberLifestyleRecord() {
        return memberLifestyleRecord;
    }

    public void setMemberLifestyleRecord(final List<MemberLifestyleRecord> memberLifestyleRecord) {
        this.memberLifestyleRecord = memberLifestyleRecord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MemberLifestyleInfo{");
        sb.append("memberLifestyleRecord=").append(memberLifestyleRecord);
        sb.append('}');
        return sb.toString();
    }
}
