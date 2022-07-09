
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
    "GivenName",
    "Surname",
    "Prefix"
})
public class Name implements Serializable {
	
	private static final long serialVersionUID = -7975993629903141127L;

	@JsonProperty("GivenName")
    private String givenName;
	
    @JsonProperty("Surname")
    private String surName;
    
    @JsonProperty("Prefix")
    private String prefix;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("GivenName")
	public String getGivenName() {
		return givenName;
	}

    @JsonProperty("GivenName")
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

    @JsonProperty("Surname")
	public String getSurName() {
		return surName;
	}

    @JsonProperty("Surname")
	public void setSurName(String surName) {
		this.surName = surName;
	}

    @JsonProperty("Prefix")
	public String getPrefix() {
		return prefix;
	}

    @JsonProperty("Prefix")
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
