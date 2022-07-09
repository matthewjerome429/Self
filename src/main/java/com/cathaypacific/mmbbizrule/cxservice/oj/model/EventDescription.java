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
    "Event_Name",
    "Full",
    "Inclusion",
    "Operating_Details",
    "Additional_Instructions",
    "Small",
    "Essential"
})
public class EventDescription implements Serializable {
	
	private static final long serialVersionUID = -62192272865616271L;

	@JsonProperty("Event_Name")
	private String eventName;
	
	@JsonProperty("Full")
	private String full;
	
	@JsonProperty("Inclusion")
	private String inclusion;
	
	@JsonProperty("Operating_Details")
	private String operatingDetails;
	
	@JsonProperty("Additional_Instructions")
	private String additionalInstructions;
	
	@JsonProperty("Small")
	private String small;
	
	@JsonProperty("Essential")
	private String essential;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("Event_Name")
	public String getEventName() {
		return eventName;
	}

	@JsonProperty("Event_Name")
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@JsonProperty("Full")
	public String getFull() {
		return full;
	}

	@JsonProperty("Full")
	public void setFull(String full) {
		this.full = full;
	}
	
	@JsonProperty("Inclusion")
	public String getInclusion() {
		return inclusion;
	}
	
	@JsonProperty("Inclusion")
	public void setInclusion(String inclusion) {
		this.inclusion = inclusion;
	}

	@JsonProperty("Operating_Details")
	public String getOperatingDetails() {
		return operatingDetails;
	}

	@JsonProperty("Operating_Details")
	public void setOperatingDetails(String operatingDetails) {
		this.operatingDetails = operatingDetails;
	}

	@JsonProperty("Small")
	public String getSmall() {
		return small;
	}

	@JsonProperty("Small")
	public void setSmall(String small) {
		this.small = small;
	}

	@JsonProperty("Essential")
	public String getEssential() {
		return essential;
	}

	@JsonProperty("Essential")
	public void setEssential(String essential) {
		this.essential = essential;
	}

	@JsonProperty("Additional_Instructions")
	public String getAdditionalInstructions() {
		return additionalInstructions;
	}

	@JsonProperty("Additional_Instructions")
	public void setAdditionalInstructions(String additionalInstructions) {
		this.additionalInstructions = additionalInstructions;
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
