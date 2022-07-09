
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
    "CheckInDate",
    "CheckOutDate",
    "Duration",
    "Adults",
    "Children",
    "Infants",
    "Option"
})
public class Room implements Serializable{

	private static final long serialVersionUID = -5059418194546242670L;

	@JsonProperty("CheckInDate")
    private String checkInDate;
    
    @JsonProperty("CheckOutDate")
    private String checkOutDate;
    
    @JsonProperty("Duration")
    private String duration;
    
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

    @JsonProperty("CheckInDate")
    public String getCheckInDate() {
        return checkInDate;
    }

    @JsonProperty("CheckInDate")
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    @JsonProperty("CheckOutDate")
    public String getCheckOutDate() {
        return checkOutDate;
    }

    @JsonProperty("CheckOutDate")
    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @JsonProperty("Duration")
    public String getDuration() {
        return duration;
    }

    @JsonProperty("Duration")
    public void setDuration(String duration) {
        this.duration = duration;
    }

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

	@JsonProperty("Option")
    public List<RoomOption> getOption() {
        return option;
    }

    @JsonProperty("Option")
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
