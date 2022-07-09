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
    "BookingReference",
    "Name",
    "URLPath",
    "Descriptions",
    "Amenities",
    "Address",
    "Rooms"
})
public class PackageHotel implements Serializable {
	
	private static final long serialVersionUID = 3054401135871918517L;
	
	@JsonProperty("BookingReference")
    private String bookingReference;
	
    @JsonProperty("Name")
    private String name;
    
    @JsonProperty("URLPath")
    private String uRLPath;
    
    @JsonProperty("Descriptions")
    private PackageDescriptions descriptions;
    
    @JsonProperty("Amenities")
    private Amenities amenities;
    
    @JsonProperty("Address")
    private Address address;
    
    @JsonProperty("Rooms")
    private List<RoomInfo> roomInfo;
    
    @JsonProperty("Airports")
    private HotelAirports airports;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
    
    @JsonProperty("BookingReference")
	public String getBookingReference() {
		return bookingReference;
	}

    @JsonProperty("BookingReference")
	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}

    @JsonProperty("Name")
	public String getName() {
		return name;
	}

    @JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

    @JsonProperty("URLPath")
	public String getuRLPath() {
		return uRLPath;
	}

    @JsonProperty("URLPath")
	public void setuRLPath(String uRLPath) {
		this.uRLPath = uRLPath;
	}

    @JsonProperty("Descriptions")
	public PackageDescriptions getDescriptions() {
		return descriptions;
	}

    @JsonProperty("Descriptions")
	public void setDescriptions(PackageDescriptions descriptions) {
		this.descriptions = descriptions;
	}

    @JsonProperty("Amenities")
	public Amenities getAmenities() {
		return amenities;
	}

    @JsonProperty("Amenities")
	public void setAmenities(Amenities amenities) {
		this.amenities = amenities;
	}

    @JsonProperty("Address")
	public Address getAddress() {
		return address;
	}

    @JsonProperty("Address")
	public void setAddress(Address address) {
		this.address = address;
	}

    @JsonProperty("Rooms")
	public List<RoomInfo> getRoomInfo() {
		return roomInfo;
	}

    @JsonProperty("Rooms")
	public void setRoomInfo(List<RoomInfo> roomInfo) {
		this.roomInfo = roomInfo;
	}
    
    @JsonProperty("Airports")
    public HotelAirports getAirports() {
		return airports;
	}
    
    @JsonProperty("Airports")
	public void setAirports(HotelAirports airports) {
		this.airports = airports;
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
