package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model;

import java.io.Serializable;

public class PNREligibilityRequest implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6691835827172659413L;
    private String pnrReply;
    private String jobId;

    public String getPnrReply() {
        return pnrReply;
    }

    public void setPnrReply(String pnrReply) {
        this.pnrReply = pnrReply;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

}
