package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request;

public class ClsAPIRequest {

    private String applicationName;

    private String correlationId;

    private UserInformation userInformation;

    // getPreference
    private String idNumber;

    // retrieveMemberCobrand, retrieveMemberPromoTarget
    private String memberNumber;

    // default
    private String memberIdOrUsername;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final String correlationId) {
        this.correlationId = correlationId;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(final UserInformation userInformation) {
        this.userInformation = userInformation;
    }

	public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(final String idNumber) {
        this.idNumber = idNumber;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(final String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getMemberIdOrUsername() {
        return memberIdOrUsername;
    }

    public void setMemberIdOrUsername(final String memberIdOrUsername) {
        this.memberIdOrUsername = memberIdOrUsername;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClsAPIRequest{");
        sb.append("applicationName='").append(applicationName).append('\'');
        sb.append(", correlationId='").append(correlationId).append('\'');
        sb.append(", userInformation=").append(userInformation);
        sb.append(", idNumber='").append(idNumber).append('\'');
        sb.append(", memberNumber='").append(memberNumber).append('\'');
        sb.append(", memberIdOrUsername='").append(memberIdOrUsername).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
