package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;
import java.util.List;

public class Passenger implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 8129716196360331248L;
    // E PETER
    private String firstname;
    // TEST
    private String lastname;
    // collection of Segment.
    private List<Segment> segments;
    // collection of Priority.
    private List<Priority> priorities;
    // passenger type
    private String passengerType;
    // passenger id
    private String PT;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public List<Priority> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<Priority> priorities) {
        this.priorities = priorities;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getPT() {
        return PT;
    }

    public void setPT(String pT) {
        PT = pT;
    }

}
