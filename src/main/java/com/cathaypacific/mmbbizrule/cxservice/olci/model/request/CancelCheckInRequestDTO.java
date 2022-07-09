package com.cathaypacific.mmbbizrule.cxservice.olci.model.request;

import java.util.List;

public class CancelCheckInRequestDTO {


    private String journeyId;

    private List<String> uniqueCustomerIds;

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public List<String> getUniqueCustomerIds() {
        return uniqueCustomerIds;
    }

    public void setUniqueCustomerIds(List<String> uniqueCustomerIds) {
        this.uniqueCustomerIds = uniqueCustomerIds;
    }
}
