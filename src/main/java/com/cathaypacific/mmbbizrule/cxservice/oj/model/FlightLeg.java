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
    "Duration",
    "CabinClass",
    "CabinClassCode",
    "FlightNumber",
    "AircraftEquipmentType",
    "ResBookDesigCode",
    "MarketingAirline",
    "OperatingAirline",
    "Departure",
    "Arrival"
})
public class FlightLeg implements Serializable {

	private static final long serialVersionUID = 6891482539288440886L;
	
	@JsonProperty("Duration")
    private String duration;
	
	@JsonProperty("CabinClass")
    private String cabinClass;
	
	@JsonProperty("CabinClassCode")
    private String cabinClassCode;
	
	@JsonProperty("FlightNumber")
    private String flightNumber;
	
	@JsonProperty("AircraftEquipmentType")
    private String aircraftEquipmentType;
	
	@JsonProperty("ResBookDesigCode")
    private String resBookDesigCode;
	
	@JsonProperty("MarketingAirline")
    private String marketingAirline;
	
	@JsonProperty("OperatingAirline")
    private String operatingAirline;
	
	@JsonProperty("Departure")
    private FlightOD departure;
	
	@JsonProperty("Arrival")
    private FlightOD arrival;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("Duration")
	public String getDuration() {
		return duration;
	}

	@JsonProperty("Duration")
	public void setDuration(String duration) {
		this.duration = duration;
	}

	@JsonProperty("CabinClass")
	public String getCabinClass() {
		return cabinClass;
	}

	@JsonProperty("CabinClass")
	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
	}

	@JsonProperty("CabinClassCode")
	public String getCabinClassCode() {
		return cabinClassCode;
	}

	@JsonProperty("CabinClassCode")
	public void setCabinClassCode(String cabinClassCode) {
		this.cabinClassCode = cabinClassCode;
	}

	@JsonProperty("FlightNumber")
	public String getFlightNumber() {
		return flightNumber;
	}

	@JsonProperty("FlightNumber")
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	@JsonProperty("AircraftEquipmentType")
	public String getAircraftEquipmentType() {
		return aircraftEquipmentType;
	}

	@JsonProperty("AircraftEquipmentType")
	public void setAircraftEquipmentType(String aircraftEquipmentType) {
		this.aircraftEquipmentType = aircraftEquipmentType;
	}

	@JsonProperty("ResBookDesigCode")
	public String getResBookDesigCode() {
		return resBookDesigCode;
	}

	@JsonProperty("ResBookDesigCode")
	public void setResBookDesigCode(String resBookDesigCode) {
		this.resBookDesigCode = resBookDesigCode;
	}

	@JsonProperty("MarketingAirline")
	public String getMarketingAirline() {
		return marketingAirline;
	}

	@JsonProperty("MarketingAirline")
	public void setMarketingAirline(String marketingAirline) {
		this.marketingAirline = marketingAirline;
	}

	@JsonProperty("OperatingAirline")
	public String getOperatingAirline() {
		return operatingAirline;
	}

	@JsonProperty("OperatingAirline")
	public void setOperatingAirline(String operatingAirline) {
		this.operatingAirline = operatingAirline;
	}

	@JsonProperty("Departure")
	public FlightOD getDeparture() {
		return departure;
	}

	@JsonProperty("Departure")
	public void setDeparture(FlightOD departure) {
		this.departure = departure;
	}

	@JsonProperty("Arrival")
	public FlightOD getArrival() {
		return arrival;
	}

	@JsonProperty("Arrival")
	public void setArrival(FlightOD arrival) {
		this.arrival = arrival;
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
