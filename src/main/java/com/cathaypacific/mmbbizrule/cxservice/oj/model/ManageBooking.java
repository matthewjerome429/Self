
package com.cathaypacific.mmbbizrule.cxservice.oj.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "SuperPNR_ID",
    "Booking"
})
public class ManageBooking implements Serializable {

	private static final long serialVersionUID = -7764825415310162681L;
	
	@JsonProperty("SuperPNR_ID")
    private String superPNRID;
	
    @JsonProperty("Booking")
    private List<OJBookingResponseDTO> booking = null;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("SuperPNR_ID")
    public String getSuperPNRID() {
        return superPNRID;
    }

    @JsonProperty("SuperPNR_ID")
    public void setSuperPNRID(String superPNRID) {
        this.superPNRID = superPNRID;
    }

    @JsonProperty("Booking")
    public List<OJBookingResponseDTO> getBooking() {
        return booking;
    }

    @JsonProperty("Booking")
    public void setBooking(List<OJBookingResponseDTO> booking) {
        this.booking = booking;
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
