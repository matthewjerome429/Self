package com.cathaypacific.mmbbizrule.cxservice.oj.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Duration",
    "MarketingAirline",
    "OperatingAirline",
    "Departure",
    "Arrival"
})
public class Summary implements Serializable{
	
private static final long serialVersionUID = 6891482539288440886L;
	
	@JsonProperty("Duration")
    private String duration;
	
	@JsonProperty("MarketingAirline")
    private String marketingAirline;
	
	@JsonProperty("OperatingAirline")
    private String operatingAirline;
	
	@JsonProperty("Departure")
    private FlightOD departure;
	
	@JsonProperty("Arrival")
    private FlightOD arrival;
	
	@JsonProperty("Duration")
	public String getDuration() {
		return duration;
	}
	@JsonProperty("Duration")
	public void setDuration(String duration) {
		this.duration = duration;
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
	
	

}
