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
    "Airport"
})
public class HotelAirports implements Serializable {

	private static final long serialVersionUID = -1295606801131792318L;
	
	@JsonProperty("Airport")
	private List<HotelAirport> airports;

	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("Airport")
	public List<HotelAirport> getAirports() {
		return airports;
	}

	@JsonProperty("Airport")
	public void setAirports(List<HotelAirport> airports) {
		this.airports = airports;
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
