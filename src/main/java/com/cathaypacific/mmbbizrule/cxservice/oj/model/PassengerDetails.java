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
    "Passenger"
})
public class PassengerDetails implements Serializable {

	private static final long serialVersionUID = -6526727836918666550L;
	
	@JsonProperty("Passenger")
	private List<Passenger> Passenger;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
	
	@JsonProperty("Passenger")
	public List<Passenger> getPassenger() {
		return Passenger;
	}
	
	@JsonProperty("Passenger")
	public void setPassenger(List<Passenger> passenger) {
		Passenger = passenger;
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
