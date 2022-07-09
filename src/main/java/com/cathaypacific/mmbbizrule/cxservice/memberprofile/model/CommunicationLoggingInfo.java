package com.cathaypacific.mmbbizrule.cxservice.memberprofile.model;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CommunicationLogging;

public class CommunicationLoggingInfo extends BaseResponse {

    private CommunicationLogging communicationLogging;

    public CommunicationLogging getCommunicationLogging() {
        return communicationLogging;
    }

    public void setCommunicationLogging(final CommunicationLogging communicationLogging) {
        this.communicationLogging = communicationLogging;
    }
}
