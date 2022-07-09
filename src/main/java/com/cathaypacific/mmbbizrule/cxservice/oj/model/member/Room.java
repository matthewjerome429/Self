
package com.cathaypacific.mmbbizrule.cxservice.oj.model.member;

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
    "Index",
    "Adults",
    "Children",
    "Infants",
    "Option"
})
public class Room {

    @JsonProperty("Index")
    private String index;
    
    @JsonProperty("Adults")
    private String adults;

    @JsonProperty("Children")
    private String children;

    @JsonProperty("Infants")
    private String infants;
    
    @JsonProperty("Option")
    private List<RoomOption> option = null;

    @JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();
    
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getAdults() {
		return adults;
	}

	public void setAdults(String adults) {
		this.adults = adults;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getInfants() {
		return infants;
	}

	public void setInfants(String infants) {
		this.infants = infants;
	}

	public List<RoomOption> getOption() {
		return option;
	}

	public void setOption(List<RoomOption> option) {
		this.option = option;
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
