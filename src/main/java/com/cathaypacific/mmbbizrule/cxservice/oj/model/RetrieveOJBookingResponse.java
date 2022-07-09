
package com.cathaypacific.mmbbizrule.cxservice.oj.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ManageBooking"
})
public class RetrieveOJBookingResponse implements Serializable {

	private static final long serialVersionUID = 7547318367986014954L;
	
	@JsonProperty("ManageBooking")
    private ManageBooking manageBooking;
	
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("ManageBooking")
    public ManageBooking getManageBooking() {
        return manageBooking;
    }

    @JsonProperty("ManageBooking")
    public void setManageBooking(ManageBooking manageBooking) {
        this.manageBooking = manageBooking;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
