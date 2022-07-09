package com.cathaypacific.mmbbizrule.cxservice.oj.model.member;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightDetails implements Serializable {

	private static final long serialVersionUID = -5531382865689143387L;
	
	@JsonProperty("Departure")
	private DepartureArrivalTime departure;
	
	@JsonProperty("Arrival")
	private DepartureArrivalTime arrival;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();
	
	public DepartureArrivalTime getDeparture() {
		return departure;
	}

	public void setDeparture(DepartureArrivalTime departure) {
		this.departure = departure;
	}

	public DepartureArrivalTime getArrival() {
		return arrival;
	}

	public void setArrival(DepartureArrivalTime arrival) {
		this.arrival = arrival;
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
