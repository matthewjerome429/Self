package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request;

public class UserInformation {

    private String memberType;

    private String memberId;

    private String token;

    public final String getMemberType() {
        return memberType;
    }

    public final void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public final String getMemberId() {
        return memberId;
    }

    public final void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public final String getToken() {
        return token;
    }

    public final void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserInformation [memberType=");
        builder.append(memberType);
        builder.append(", memberId=");
        builder.append(memberId);
        builder.append(", token=");
        builder.append(token);
        builder.append("]");
        return builder.toString();
    }

}
