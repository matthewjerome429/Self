package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmexCard {

    private Double amexCpCdHolBelId;

    private String amexCpCdHolInd;

    private String amexCorpAccInd;

    public Double getAmexCpCdHolBelId() {
        return amexCpCdHolBelId;
    }

    public void setAmexCpCdHolBelId(final Double amexCpCdHolBelId) {
        this.amexCpCdHolBelId = amexCpCdHolBelId;
    }

    public String getAmexCpCdHolInd() {
        return amexCpCdHolInd;
    }

    public void setAmexCpCdHolInd(final String amexCpCdHolInd) {
        this.amexCpCdHolInd = amexCpCdHolInd;
    }

    public String getAmexCorpAccInd() {
        return amexCorpAccInd;
    }

    public void setAmexCorpAccInd(final String amexCorpAccInd) {
        this.amexCorpAccInd = amexCorpAccInd;
    }
}
