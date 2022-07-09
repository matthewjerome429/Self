package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignID {

    private String cxPecialsSubscriptionCampaignID;

    private String kaSpecialsSubscriptionCampaignID;

    private String phoneRegistrationID;

    private String recruitmentCampaignID;

    public String getCxPecialsSubscriptionCampaignID() {
        return cxPecialsSubscriptionCampaignID;
    }

    public void setCxPecialsSubscriptionCampaignID(final String cxPecialsSubscriptionCampaignID) {
        this.cxPecialsSubscriptionCampaignID = cxPecialsSubscriptionCampaignID;
    }

    public String getKaSpecialsSubscriptionCampaignID() {
        return kaSpecialsSubscriptionCampaignID;
    }

    public void setKaSpecialsSubscriptionCampaignID(final String kaSpecialsSubscriptionCampaignID) {
        this.kaSpecialsSubscriptionCampaignID = kaSpecialsSubscriptionCampaignID;
    }

    public String getPhoneRegistrationID() {
        return phoneRegistrationID;
    }

    public void setPhoneRegistrationID(final String phoneRegistrationID) {
        this.phoneRegistrationID = phoneRegistrationID;
    }

    public String getRecruitmentCampaignID() {
        return recruitmentCampaignID;
    }

    public void setRecruitmentCampaignID(final String recruitmentCampaignID) {
        this.recruitmentCampaignID = recruitmentCampaignID;
    }
}
