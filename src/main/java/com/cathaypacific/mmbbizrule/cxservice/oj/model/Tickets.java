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
    "Adults",
    "Children",
    "Infants",
    "Units",
    "Seniors",
    "Date",
    "Time"
})
public class Tickets implements Serializable {
	
	private static final long serialVersionUID = -6869997688911654572L;

	@JsonProperty("Adults")
	private String adults;
	
	@JsonProperty("Children")
	private String children;
	
	@JsonProperty("Infants")
	private String infants;
	
	@JsonProperty("Units")
	private String units;
	
	@JsonProperty("Seniors")
	private String seniors;
	
	@JsonProperty("Date")
	private String date;
	
	@JsonProperty("Time")
	private String time;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("Adults")
	public String getAdults() {
		return adults;
	}

	@JsonProperty("Adults")
	public void setAdults(String adults) {
		this.adults = adults;
	}

	@JsonProperty("Children")
	public String getChildren() {
		return children;
	}

	@JsonProperty("Children")
	public void setChildren(String children) {
		this.children = children;
	}

	@JsonProperty("Infants")
	public String getInfants() {
		return infants;
	}

	@JsonProperty("Infants")
	public void setInfants(String infants) {
		this.infants = infants;
	}

	@JsonProperty("Units")
	public String getUnits() {
		return units;
	}

	@JsonProperty("Units")
	public void setUnits(String units) {
		this.units = units;
	}

	@JsonProperty("Seniors")
	public String getSeniors() {
		return seniors;
	}

	@JsonProperty("Seniors")
	public void setSeniors(String seniors) {
		this.seniors = seniors;
	}

	@JsonProperty("Date")
	public String getDate() {
		return date;
	}

	@JsonProperty("Date")
	public void setDate(String date) {
		this.date = date;
	}

	@JsonProperty("Time")
	public String getTime() {
		return time;
	}

	@JsonProperty("Time")
	public void setTime(String time) {
		this.time = time;
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
