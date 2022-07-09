package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;


import java.util.List;

public class MessageCollectionResponseDTO {

    private String collectionId;

    private List<MessageDto> messages;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }
}
