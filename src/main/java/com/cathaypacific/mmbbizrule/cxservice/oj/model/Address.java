
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
    "Compressed",
    "CityName"
})
public class Address implements Serializable{

	private static final long serialVersionUID = -5762963075866908999L;
	
	@JsonProperty("Compressed")
    private String compressed;
	
	@JsonProperty("CityName")
	private List<CityNameInfo> cityNameInfos;
	
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("Compressed")
    public String getCompressed() {
        return compressed;
    }

    @JsonProperty("Compressed")
    public void setCompressed(String compressed) {
        this.compressed = compressed;
    }
    
    @JsonProperty("CityName")
	public List<CityNameInfo> getCityNameInfos() {
		return cityNameInfos;
	}
    
    @JsonProperty("CityName")
	public void setCityNameInfos(List<CityNameInfo> cityNameInfos) {
		this.cityNameInfos = cityNameInfos;
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
