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
    "IATA",
    "Airport"
})
public class EventAirport implements Serializable {
	
	private static final long serialVersionUID = 725879544078465898L;

	@JsonProperty("IATA")
	private String iata;
	
	@JsonProperty("Airport")
	private String airPort;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonProperty("IATA")
	public String getIata() {
		return iata;
	}
	
	@JsonProperty("IATA")
	public void setIata(String iata) {
		this.iata = iata;
	}

	@JsonProperty("Airport")
	public String getAirPort() {
		return airPort;
	}

	@JsonProperty("Airport")
	public void setAirPort(String airPort) {
		this.airPort = airPort;
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
