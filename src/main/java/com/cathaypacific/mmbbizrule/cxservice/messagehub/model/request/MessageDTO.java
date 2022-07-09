package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

public class MessageDTO {

    private String msgId;

    private StatusRequestDto status;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public StatusRequestDto getStatus() {
        return status;
    }

    public void setStatus(StatusRequestDto status) {
        this.status = status;
    }
}
