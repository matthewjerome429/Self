package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.util.List;

public class JourneyDTO {

    /**
     * journey Id
     */
    private String journeyId;
    /** Rloc */
    private String rloc;

    /** passengers */
    private List<PassengerDTO> passengers;

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getRloc() {
        return rloc;
    }

    public void setRloc(String rloc) {
        this.rloc = rloc;
    }

    public List<PassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }
}
