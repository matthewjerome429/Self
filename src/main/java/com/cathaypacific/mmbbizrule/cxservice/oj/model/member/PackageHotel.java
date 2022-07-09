package com.cathaypacific.mmbbizrule.cxservice.oj.model.member;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PackageHotel implements Serializable{

	private static final long serialVersionUID = 4278123477583267018L;

	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Location")
	private String location;
	
	@JsonProperty("BookingReference")
	private String bookingReference;
	
	@JsonProperty("BookingStatus")
	private String bookingStatus;
	
	@JsonProperty("StartDate")
	private String startDate;
	
	@JsonProperty("EndDate")
	private String endDate;
	
	@JsonProperty("Rooms")
	private List<RoomInfo> roomInfos;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<RoomInfo> getRoomInfos() {
		return roomInfos;
	}

	public void setRoomInfos(List<RoomInfo> roomInfos) {
		this.roomInfos = roomInfos;
	}

	public String getBookingReference() {
		return bookingReference;
	}

	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
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
