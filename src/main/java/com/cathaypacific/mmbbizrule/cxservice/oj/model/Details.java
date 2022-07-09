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
    "Name",
    "Destination",
    "Airport",
    "Images"
})
public class Details implements Serializable {
	
	private static final long serialVersionUID = 8991750265264196709L;

	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Destination")
	private Destination destination;
	
	@JsonProperty("Airport")
	private EventAirport airPort;
	
	@JsonProperty("Images")
	private Images images;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
	
	@JsonProperty("Name")
	public String getName() {
		return name;
	}
	
	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty("Destination")
	public Destination getDestination() {
		return destination;
	}
	
	@JsonProperty("Destination")
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	
	@JsonProperty("Airport")
	public EventAirport getAirPort() {
		return airPort;
	}
	
	@JsonProperty("Airport")
	public void setAirPort(EventAirport airPort) {
		this.airPort = airPort;
	}

	@JsonProperty("Images")
	public Images getImages() {
		return images;
	}

	@JsonProperty("Images")
	public void setImages(Images images) {
		this.images = images;
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
