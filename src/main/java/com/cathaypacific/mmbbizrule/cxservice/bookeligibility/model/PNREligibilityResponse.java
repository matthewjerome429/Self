package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;
import java.util.List;

public class PNREligibilityResponse implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5968010596225806105L;
    // PNR rLoc
    private String rloc;
    // string provided in request
    private String jobId;
    // collection of Passenger.
    private List<Passenger> passengers;

    public String getRloc() {
        return rloc;
    }

    public void setRloc(String rloc) {
        this.rloc = rloc;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

}
