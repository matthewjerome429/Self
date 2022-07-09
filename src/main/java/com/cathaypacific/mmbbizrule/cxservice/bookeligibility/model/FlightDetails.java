package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;
import java.util.List;

public class FlightDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8942967950919123964L;

    // Departure Airport Code Format:3 digits IATA airport code Example:HKG
    private String origin;
    // Arrival Airport Code Format:3 digits IATA airport code Example:TPE
    private String destination;
    // Meal Pre-select eligibility
    private Boolean eligible;
    private List<Services> services;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Boolean getEligible() {
        return eligible;
    }

    public void setEligible(Boolean eligible) {
        this.eligible = eligible;
    }

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

}
