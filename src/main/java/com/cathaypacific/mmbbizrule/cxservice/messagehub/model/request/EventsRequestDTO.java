package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request;

import java.util.List;

import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class EventsRequestDTO {

    @Pattern(regexp = "^[A-Za-z0-9]{6,7}$",message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
    private String rloc;
    
    private List<String> eventTypes;

    public String getRloc() {
        return rloc;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setRloc(String rloc) {
        this.rloc = rloc;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }
    

}
