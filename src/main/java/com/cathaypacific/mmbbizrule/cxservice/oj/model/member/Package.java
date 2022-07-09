package com.cathaypacific.mmbbizrule.cxservice.oj.model.member;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Package implements Serializable{

	private static final long serialVersionUID = 3628471127960251045L;
	
	@JsonProperty("Flight")
	private Flight flight;
	
	@JsonProperty("Hotel")
	private PackageHotel hotel;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public PackageHotel getHotel() {
		return hotel;
	}

	public void setHotel(PackageHotel hotel) {
		this.hotel = hotel;
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
