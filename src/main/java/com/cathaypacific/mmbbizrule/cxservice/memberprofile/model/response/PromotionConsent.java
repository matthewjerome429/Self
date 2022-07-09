package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PromotionConsent {

    private Boolean cxPecialsConsentIndicator;

    private Boolean kaSPecialsConsentIndicator;

    private Boolean receivingCXPromotionConsent;

    private Boolean receivingKAPromotionConsent;

    private Boolean serviceConvenienceConsent;

    public Boolean getCxPecialsConsentIndicator() {
        return cxPecialsConsentIndicator;
    }

    public void setCxPecialsConsentIndicator(final Boolean cxPecialsConsentIndicator) {
        this.cxPecialsConsentIndicator = cxPecialsConsentIndicator;
    }

    public Boolean getKaSPecialsConsentIndicator() {
        return kaSPecialsConsentIndicator;
    }

    public void setKaSPecialsConsentIndicator(final Boolean kaSPecialsConsentIndicator) {
        this.kaSPecialsConsentIndicator = kaSPecialsConsentIndicator;
    }

    public Boolean getReceivingCXPromotionConsent() {
        return receivingCXPromotionConsent;
    }

    public void setReceivingCXPromotionConsent(final Boolean receivingCXPromotionConsent) {
        this.receivingCXPromotionConsent = receivingCXPromotionConsent;
    }

    public Boolean getReceivingKAPromotionConsent() {
        return receivingKAPromotionConsent;
    }

    public void setReceivingKAPromotionConsent(final Boolean receivingKAPromotionConsent) {
        this.receivingKAPromotionConsent = receivingKAPromotionConsent;
    }

    public Boolean getServiceConvenienceConsent() {
        return serviceConvenienceConsent;
    }

    public void setServiceConvenienceConsent(final Boolean serviceConvenienceConsent) {
        this.serviceConvenienceConsent = serviceConvenienceConsent;
    }
}
