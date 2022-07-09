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
    "Alt",
    "URL",
    "Index",
    "Format"
})
public class Image implements Serializable {
	
	private static final long serialVersionUID = 193593477967484222L;

	@JsonProperty("Alt")
	private String alt;
	
	@JsonProperty("URL")
	private String url;
	
	@JsonProperty("Index")
	private String index;
	
	@JsonProperty("Format")
	private String format;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("Alt")
	public String getAlt() {
		return alt;
	}

	@JsonProperty("Alt")
	public void setAlt(String alt) {
		this.alt = alt;
	}

	@JsonProperty("URL")
	public String getUrl() {
		return url;
	}

	@JsonProperty("URL")
	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty("Index")
	public String getIndex() {
		return index;
	}

	@JsonProperty("Index")
	public void setIndex(String index) {
		this.index = index;
	}

	@JsonProperty("Format")
	public String getFormat() {
		return format;
	}

	@JsonProperty("Format")
	public void setFormat(String format) {
		this.format = format;
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
