
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
    "Format",
    "URL"
})
public class Document implements Serializable {

	private static final long serialVersionUID = 9216457141414737956L;
	
	@JsonProperty("Type")
    private String type;
	
    @JsonProperty("Format")
    private String format;
    
    @JsonProperty("URL")
    private String url;
    
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

    @JsonProperty("Format")
    public String getFormat() {
        return format;
    }

    @JsonProperty("Format")
    public void setFormat(String format) {
        this.format = format;
    }

    @JsonProperty("URL")
    public String getURL() {
        return url;
    }

    @JsonProperty("URL")
    public void setURL(String uRL) {
        this.url = uRL;
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
