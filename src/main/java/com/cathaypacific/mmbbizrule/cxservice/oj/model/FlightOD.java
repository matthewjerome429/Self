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
    "IATA",
    "Airport",
    "Terminal",
    "Date",
    "Time"
})
public class FlightOD implements Serializable {

	private static final long serialVersionUID = -5378704394049925041L;
	
	@JsonProperty("IATA")
    private String iata;
	
	@JsonProperty("Airport")
    private String airport;
	
	@JsonProperty("Terminal")
    private String terminal;
	
	@JsonProperty("Date")
    private String date;
	
	@JsonProperty("Time")
    private String time;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("IATA")
	public String getIata() {
		return iata;
	}

	@JsonProperty("IATA")
	public void setIata(String iata) {
		this.iata = iata;
	}

	@JsonProperty("Airport")
	public String getAirport() {
		return airport;
	}

	@JsonProperty("Airport")
	public void setAirport(String airport) {
		this.airport = airport;
	}

	@JsonProperty("Terminal")
	public String getTerminal() {
		return terminal;
	}

	@JsonProperty("Terminal")
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	@JsonProperty("Date")
	public String getDate() {
		return date;
	}

	@JsonProperty("Date")
	public void setDate(String date) {
		this.date = date;
	}

	@JsonProperty("Time")
	public String getTime() {
		return time;
	}

	@JsonProperty("Time")
	public void setTime(String time) {
		this.time = time;
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
