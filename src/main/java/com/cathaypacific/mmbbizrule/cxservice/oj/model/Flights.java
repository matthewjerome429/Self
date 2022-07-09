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
    "CheckInTime",
    "Mode",
    "Flight"
})
public class Flights implements Serializable {

	private static final long serialVersionUID = 2749532643881351272L;
	
	@JsonProperty("CheckInTime")
    private String checkInTime;
	
	@JsonProperty("Mode")
	private String mode;
	
	@JsonProperty("Flight")
	private List<Flight> flight;
	
	@JsonProperty("PassengerDetails")
	private PassengerDetails PassengerDetails;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("CheckInTime")
	public String getCheckInTime() {
		return checkInTime;
	}

	@JsonProperty("CheckInTime")
	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}
	
	@JsonProperty("Mode")
	public String getMode() {
		return mode;
	}

	@JsonProperty("Mode")
	public void setMode(String mode) {
		this.mode = mode;
	}

	@JsonProperty("Flight")
	public List<Flight> getFlight() {
		return flight;
	}

	@JsonProperty("Flight")
	public void setFlight(List<Flight> flight) {
		this.flight = flight;
	}
	
	@JsonProperty("PassengerDetails")
	public PassengerDetails getPassengerDetails() {
		return PassengerDetails;
	}

	@JsonProperty("PassengerDetails")
	public void setPassengerDetails(PassengerDetails passengerDetails) {
		PassengerDetails = passengerDetails;
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
