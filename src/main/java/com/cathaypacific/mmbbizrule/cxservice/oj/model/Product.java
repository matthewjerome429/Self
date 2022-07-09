
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
    "BookingStatus",
    "Package",
    "Hotel",
    "Event"
})
public class Product implements Serializable {

	private static final long serialVersionUID = 464548747266268025L;
	
	@JsonProperty("Type")
    private String type;
	
	@JsonProperty("BookingStatus")
	private String bookingStatus;
	
    @JsonProperty("Package")
    private Package _package;
    
    @JsonProperty("Hotel")
    private Hotel hotel;
    
    @JsonProperty("Event")
    private Event event;
    
    @JsonProperty("Flight")
    private ProductFlights flight;
    
    @JsonProperty("BookingDate")
    private String bookingDate;
    
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
    
    @JsonProperty("BookingStatus")
    public String getBookingStatus() {
		return bookingStatus;
	}

    @JsonProperty("BookingStatus")
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	@JsonProperty("Package")
    public Package getPackage() {
        return _package;
    }

    @JsonProperty("Package")
    public void setPackage(Package _package) {
        this._package = _package;
    }

    @JsonProperty("Hotel")
    public Hotel getHotel() {
		return hotel;
	}

    @JsonProperty("Hotel")
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
    
    @JsonProperty("Event")
	public Event getEvent() {
		return event;
	}
    
    @JsonProperty("Event")
	public void setEvent(Event event) {
		this.event = event;
	}
    
    @JsonProperty("BookingDate")
	public String getBookingDate() {
		return bookingDate;
	}
	
    @JsonProperty("BookingDate")
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    @JsonProperty("Flight")
	public ProductFlights getFlight() {
		return flight;
	}
    @JsonProperty("Flight")
	public void setFlight(ProductFlights flight) {
		this.flight = flight;
	}
   
}
