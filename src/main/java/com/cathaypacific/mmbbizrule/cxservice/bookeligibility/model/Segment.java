package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;

public class Segment implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 580438273663993883L;
    // segment id from pnr
    private String ST;
    // flight (CX470)
    private String flight;
    // flightDate (2019-12-02)
    private String flightDate;
    // STD in local time (YYYY-MM-DD HH:mm:ss)
    private String stdLocal;
    // STD in GMT+0 (2019-12-02T01:45:00Z)
    private String stdGmt;
    // origin airport (HKG)
    private String origin;
    // destination airport (TPE)
    private String destination;
    // Eligibility.
    private Eligibility eligibilities;

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getStdLocal() {
        return stdLocal;
    }

    public void setStdLocal(String stdLocal) {
        this.stdLocal = stdLocal;
    }

    public String getStdGmt() {
        return stdGmt;
    }

    public void setStdGmt(String stdGmt) {
        this.stdGmt = stdGmt;
    }

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

    public Eligibility getEligibilities() {
        return eligibilities;
    }

    public void setEligibilities(Eligibility eligibilities) {
        this.eligibilities = eligibilities;
    }

    public String getST() {
        return ST;
    }

    public void setST(String sT) {
        ST = sT;
    }
    
}
