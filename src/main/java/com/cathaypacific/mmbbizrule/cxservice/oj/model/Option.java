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
    "BookingReference",
    "FlightLeg"
})
public class Option implements Serializable {

	private static final long serialVersionUID = -883032644487567057L;
	
	@JsonProperty("BookingReference")
    private String bookingReference;
	
	@JsonProperty("FlightLeg")
    private List<FlightLeg> flightLeg;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("BookingReference")
	public String getBookingReference() {
		return bookingReference;
	}

	@JsonProperty("BookingReference")
	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}
	
	@JsonProperty("FlightLeg")
	public List<FlightLeg> getFlightLeg() {
		return flightLeg;
	}

	@JsonProperty("FlightLeg")
	public void setFlightLeg(List<FlightLeg> flightLeg) {
		this.flightLeg = flightLeg;
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
