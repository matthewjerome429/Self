package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

public class MemberAffiliationRecord {

    private String affiliatedMemberNumber;

    private String relationshipTypeCode;

    public String getAffiliatedMemberNumber() {
        return affiliatedMemberNumber;
    }

    public void setAffiliatedMemberNumber(final String affiliatedMemberNumber) {
        this.affiliatedMemberNumber = affiliatedMemberNumber;
    }

    public String getRelationshipTypeCode() {
        return relationshipTypeCode;
    }

    public void setRelationshipTypeCode(final String relationshipTypeCode) {
        this.relationshipTypeCode = relationshipTypeCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MemberAffiliationRecord{");
        sb.append("affiliatedMemberNumber='").append(affiliatedMemberNumber).append('\'');
        sb.append(", relationshipTypeCode='").append(relationshipTypeCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
