
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
    "REF",
    "searchedFlightType",
    "Passengers",
    "Flights",
    "Hotel"
})
public class Package implements Serializable {

	private static final long serialVersionUID = -6844474375091755795L;
	
	@JsonProperty("Type")
    private String type;
	
    @JsonProperty("REF")
    private String ref;
    
    @JsonProperty("searchedFlightType")
    private String searchedFlightType;
    
    @JsonProperty("Passengers")
    private Passengers passengers;
    
    @JsonProperty("Flights")
    private Flights flights;
    
    @JsonProperty("Hotel")
    private PackageHotel hotel;
    
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

    @JsonProperty("REF")
    public String getREF() {
        return ref;
    }

    @JsonProperty("REF")
    public void setREF(String ref) {
        this.ref = ref;
    }

    @JsonProperty("searchedFlightType")
    public String getSearchedFlightType() {
        return searchedFlightType;
    }

    @JsonProperty("searchedFlightType")
    public void setSearchedFlightType(String searchedFlightType) {
        this.searchedFlightType = searchedFlightType;
    }

    @JsonProperty("Passengers")
    public Passengers getPassengers() {
        return passengers;
    }

    @JsonProperty("Passengers")
    public void setPassengers(Passengers passengers) {
        this.passengers = passengers;
    }

    @JsonProperty("Flights")
    public Flights getFlights() {
        return flights;
    }

    @JsonProperty("Flights")
    public void setFlights(Flights flights) {
        this.flights = flights;
    }

    @JsonProperty("Hotel")
    public PackageHotel getHotel() {
        return hotel;
    }

    @JsonProperty("Hotel")
    public void setHotel(PackageHotel hotel) {
        this.hotel = hotel;
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
