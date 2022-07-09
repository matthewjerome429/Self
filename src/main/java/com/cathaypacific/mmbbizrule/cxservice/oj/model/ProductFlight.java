package com.cathaypacific.mmbbizrule.cxservice.oj.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Duration",
    "DirectionInd",
    "Summary",
    "FlightLeg",
    "JourneyId"
})
public class ProductFlight implements Serializable{
	
	private static final long serialVersionUID = -6058751690358101375L;
	
	@JsonProperty("Duration")
	private String duration;
	
	@JsonProperty("DirectionInd")
	private String directionInd;
	
	@JsonProperty("JourneyId")
	private String journeyId;
	
	@JsonProperty("Summary")
	private Summary summary;
	
	@JsonProperty("FlightLeg")
    private List<FlightLeg> flightLeg;
	
	@JsonProperty("Duration")
	public String getDuration() {
		return duration;
	}
	@JsonProperty("Duration")
	public void setDuration(String duration) {
		this.duration = duration;
	}
	@JsonProperty("DirectionInd")
	public String getDirectionInd() {
		return directionInd;
	}
	@JsonProperty("DirectionInd")
	public void setDirectionInd(String directionInd) {
		this.directionInd = directionInd;
	}
	@JsonProperty("JourneyId")
	public String getJourneyId() {
		return journeyId;
	}
	@JsonProperty("JourneyId")
	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}
	@JsonProperty("Summary")
	public Summary getSummary() {
		return summary;
	}
	@JsonProperty("Summary")
	public void setSummary(Summary summary) {
		this.summary = summary;
	}
	@JsonProperty("FlightLeg")
	public List<FlightLeg> getFlightLeg() {
		return flightLeg;
	}
	@JsonProperty("FlightLeg")
	public void setFlightLeg(List<FlightLeg> flightLeg) {
		this.flightLeg = flightLeg;
	}
	
	

}
