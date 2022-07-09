package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class EventsResponseDTO implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 8261168678552062917L;
    @ApiModelProperty(value = "events under passenger segment")
    private List<PassengerSegmentEvent> events;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<PassengerSegmentEvent> getEvents() {
        return events;
    }

    public void setEvents(List<PassengerSegmentEvent> events) {
        this.events = events;
    }
    
    
}
