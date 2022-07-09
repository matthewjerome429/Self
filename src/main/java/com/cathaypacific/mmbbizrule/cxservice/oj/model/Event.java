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
    "Reference",
    "SupplierCode",
    "BookingReference",
    "Details",
    "Tickets",
    "Description",
    "GuestDetails"
})
public class Event implements Serializable {
	
	private static final long serialVersionUID = -6430076989695880817L;

	@JsonProperty("Reference")
	private String reference;
	
	@JsonProperty("SupplierCode")
	private String supplierCode;
	
	@JsonProperty("BookingReference")
	private String bookingReference;
	
	@JsonProperty("Details")
	private Details details;
	
	@JsonProperty("Tickets")
	private Tickets tickets;
	
	@JsonProperty("Description")
	private EventDescription description;
	
	@JsonProperty("GuestDetails")
	private EventGuestDetails guestDetails;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
	
	@JsonProperty("Reference")
	public String getReference() {
		return reference;
	}

	@JsonProperty("Reference")
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	@JsonProperty("SupplierCode")
	public String getSupplierCode() {
		return supplierCode;
	}
	
	@JsonProperty("SupplierCode")
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	
	@JsonProperty("BookingReference")
	public String getBookingReference() {
		return bookingReference;
	}
	
	@JsonProperty("BookingReference")
	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}
	
	@JsonProperty("Details")
	public Details getDetails() {
		return details;
	}
	
	@JsonProperty("Details")
	public void setDetails(Details details) {
		this.details = details;
	}
	
	@JsonProperty("Tickets")
	public Tickets getTickets() {
		return tickets;
	}
	
	@JsonProperty("Tickets")
	public void setTickets(Tickets tickets) {
		this.tickets = tickets;
	}
	
	@JsonProperty("Description")
	public EventDescription getDescription() {
		return description;
	}
	
	@JsonProperty("Description")
	public void setDescription(EventDescription description) {
		this.description = description;
	}
	
	@JsonProperty("GuestDetails")
	public EventGuestDetails getGuestDetails() {
		return guestDetails;
	}
	
	@JsonProperty("GuestDetails")
	public void setGuestDetails(EventGuestDetails guestDetails) {
		this.guestDetails = guestDetails;
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
