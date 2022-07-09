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
    "_text"
})
public class CityNameInfo implements Serializable{

	private static final long serialVersionUID = -1829395070219048245L;
	
	@JsonProperty("_text")
	private String cityName;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
	
	@JsonProperty("_text")
	public String getCityName() {
		return cityName;
	}
	
	@JsonProperty("_text")
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
