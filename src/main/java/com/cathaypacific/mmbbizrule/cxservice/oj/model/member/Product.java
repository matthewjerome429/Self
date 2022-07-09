package com.cathaypacific.mmbbizrule.cxservice.oj.model.member;

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
    "Package",
    "Hotel",
    "Flight"
})
public class Product implements Serializable{

	private static final long serialVersionUID = 2506680320922260535L;

	@JsonProperty("Package")
	private Package packageInfo;
	
	@JsonProperty("Hotel")
	private Hotel hotel;
	
	@JsonProperty("Flight")
	private Flight flight;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	public Package getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(Package packageInfo) {
		this.packageInfo = packageInfo;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
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
