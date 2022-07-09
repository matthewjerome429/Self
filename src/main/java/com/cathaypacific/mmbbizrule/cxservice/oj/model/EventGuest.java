
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
    "Type",
    "RPH",
    "TicketCategory",
    "Name"
})
public class EventGuest implements Serializable {
	
	private static final long serialVersionUID = 5012471265600837528L;

	@JsonProperty("Type")
    private String type;
	
	@JsonProperty("RPH")
    private String rph;
	
	@JsonProperty("TicketCategory")
    private String ticketCategory;
	
	@JsonProperty("Name")
    private Name name;
	
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("Type")
	public String getType() {
		return type;
	}

    @JsonProperty("Type")
	public void setType(String type) {
		this.type = type;
	}

    @JsonProperty("RPH")
	public String getRph() {
		return rph;
	}

    @JsonProperty("RPH")
	public void setRph(String rph) {
		this.rph = rph;
	}

    @JsonProperty("TicketCategory")
	public String getTicketCategory() {
		return ticketCategory;
	}

    @JsonProperty("TicketCategory")
	public void setTicketCategory(String ticketCategory) {
		this.ticketCategory = ticketCategory;
	}

    @JsonProperty("Name")
	public Name getName() {
		return name;
	}

    @JsonProperty("Name")
	public void setName(Name name) {
		this.name = name;
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
