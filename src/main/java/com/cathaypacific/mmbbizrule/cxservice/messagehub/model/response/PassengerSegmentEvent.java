package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

public class PassengerSegmentEvent implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -6604722595666644568L;
    @ApiModelProperty(value = "passengerId in PNR")
    private String passengerId;
    @JsonIgnore
    private String passengerType;
    @ApiModelProperty(value = "segmentId in PNR")
    private String segmentId;
    @ApiModelProperty(value = "eventId from message hub")
    private String eventId;
    @ApiModelProperty(value = "eventType from message hub")
    private String eventType;
    @JsonIgnore
    private String eventCreateTime;
    @JsonIgnore
    private boolean validMessage;

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }
    
    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventCreateTime() {
        return eventCreateTime;
    }

    public void setEventCreateTime(String eventCreateTime) {
        this.eventCreateTime = eventCreateTime;
    }

    public boolean hasValidMessage() {
        return validMessage;
    }

    public void setValidMessage(boolean validMessage) {
        this.validMessage = validMessage;
    }
    
    
}
