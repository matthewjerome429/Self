
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
    "Infants"
})
public class Passengers implements Serializable {

	private static final long serialVersionUID = -6058751690358101375L;
	
	@JsonProperty("Adults")
    private String adults;
	
    @JsonProperty("Children")
    private String children;
    
    @JsonProperty("Infants")
    private String infants;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
