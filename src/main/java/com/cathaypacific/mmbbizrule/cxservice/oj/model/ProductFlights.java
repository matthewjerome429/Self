package com.cathaypacific.mmbbizrule.cxservice.oj.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "BookingReference",
    "DocumentationRequired",
    "FlightDetails"
})
public class ProductFlights implements Serializable{
	
	private static final long serialVersionUID = -6058751690358101375L;
	
	@JsonProperty("BookingReference")
	private String bookingReference;
	
	@JsonProperty("DocumentationRequired")
	private String documentationRequired;
	
	@JsonProperty("FlightDetails")
	private List<ProductFlight> flightDetails;
	
	@JsonProperty("FlightDetails")
	public List<ProductFlight> getFlightDetails() {
		return flightDetails;
	}
	@JsonProperty("FlightDetails")
	public void setFlightDetails(List<ProductFlight> flightDetails) {
		this.flightDetails = flightDetails;
	}
	@JsonProperty("BookingReference")
	public String getBookingReference() {
		return bookingReference;
	}
	@JsonProperty("BookingReference")
	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}
	@JsonProperty("DocumentationRequired")
	public String getDocumentationRequired() {
		return documentationRequired;
	}
	@JsonProperty("DocumentationRequired")
	public void setDocumentationRequired(String documentationRequired) {
		this.documentationRequired = documentationRequired;
	}
	
}
