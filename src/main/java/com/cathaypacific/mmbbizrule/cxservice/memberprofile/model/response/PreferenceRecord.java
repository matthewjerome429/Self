package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PreferenceRecord {

    private CampaignID campaignID;

    private String communicationPreferredOrigin;

    private String communicationZIPCode;

    private String mealPreference;

    private String guardian;

    private List<OtherPreferenceRecord> otherPreference;

    private PromotionConsent promotionConsent;

    private String seatPreference;

    private List<TravelDocumentRecord> travelDocument;

    public CampaignID getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(final CampaignID campaignID) {
        this.campaignID = campaignID;
    }

    public String getCommunicationPreferredOrigin() {
        return communicationPreferredOrigin;
    }

    public void setCommunicationPreferredOrigin(final String communicationPreferredOrigin) {
        this.communicationPreferredOrigin = communicationPreferredOrigin;
    }

    public String getCommunicationZIPCode() {
        return communicationZIPCode;
    }

    public void setCommunicationZIPCode(final String communicationZIPCode) {
        this.communicationZIPCode = communicationZIPCode;
    }

    public String getMealPreference() {
        return mealPreference;
    }

    public void setMealPreference(final String mealPreference) {
        this.mealPreference = mealPreference;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(final String guardian) {
        this.guardian = guardian;
    }

    public List<OtherPreferenceRecord> getOtherPreference() {
        return otherPreference;
    }

    public void setOtherPreference(final List<OtherPreferenceRecord> otherPreference) {
        this.otherPreference = otherPreference;
    }

    public PromotionConsent getPromotionConsent() {
        return promotionConsent;
    }

    public void setPromotionConsent(final PromotionConsent promotionConsent) {
        this.promotionConsent = promotionConsent;
    }

    public String getSeatPreference() {
        return seatPreference;
    }

    public void setSeatPreference(final String seatPreference) {
        this.seatPreference = seatPreference;
    }

    public List<TravelDocumentRecord> getTravelDocument() {
        return travelDocument;
    }

    public void setTravelDocument(final List<TravelDocumentRecord> travelDocument) {
        this.travelDocument = travelDocument;
    }
}
