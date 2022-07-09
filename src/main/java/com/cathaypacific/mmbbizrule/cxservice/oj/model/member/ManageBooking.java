package com.cathaypacific.mmbbizrule.cxservice.oj.model.member;

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
	"Booking"
})
public class ManageBooking implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("Booking")
	private List<OJBookingDTO> bookings;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	public List<OJBookingDTO> getBookings() {
		return bookings;
	}

	public void setBookings(List<OJBookingDTO> bookings) {
		this.bookings = bookings;
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
