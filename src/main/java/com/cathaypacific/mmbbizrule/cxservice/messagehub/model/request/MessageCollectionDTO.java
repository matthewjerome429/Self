package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

import java.util.List;

public class MessageCollectionDTO {

    private String collectionId;

    private List<MessageDTO> messages;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }
}
