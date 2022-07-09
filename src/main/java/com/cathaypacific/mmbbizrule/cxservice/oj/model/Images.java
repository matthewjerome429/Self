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
    "NumberImagesDisplay",
    "Image"
})
public class Images implements Serializable {
	
	private static final long serialVersionUID = 3659741858209845460L;

	@JsonProperty("NumberImagesDisplay")
	private String numberImagesDisplay;
	
	@JsonProperty("Image")
	private List<Image> images;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
	
	@JsonProperty("NumberImagesDisplay")
	public String getNumberImagesDisplay() {
		return numberImagesDisplay;
	}
	
	@JsonProperty("NumberImagesDisplay")
	public void setNumberImagesDisplay(String numberImagesDisplay) {
		this.numberImagesDisplay = numberImagesDisplay;
	}
	
	@JsonProperty("Image")
	public List<Image> getImages() {
		return images;
	}

	@JsonProperty("Image")
	public void setImages(List<Image> images) {
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
