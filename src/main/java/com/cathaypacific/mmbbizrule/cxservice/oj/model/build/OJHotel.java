package com.cathaypacific.mmbbizrule.cxservice.oj.model.build;

import java.util.Date;
import java.util.List;

public class OJHotel extends OJBookingOrder {
	
	/** Format of check in/out date*/
	public static final String CHECK_DATE_FORMAT = "yyyy-MM-dd";
	
	/** Format of check in/out time*/
	public static final String CHECK_TIME_FORMAT = "HH:mm";
	
	/** Format of check in/out date*/
	public static final String CHECK_DATE_TIME_FORMAT = CHECK_DATE_FORMAT + " " + CHECK_TIME_FORMAT;

	private String bookingReference;
	
	private String name;
	
	private String urlPath;
	
	private List<OJHotelDescription> descriptions;
	
	private List<OJHotelAmenity> amenities;
	
	private OJHotelPosition position;
	
	private List<OJHotelRoom> rooms;
	
	private Boolean isStayed;
	
	private Boolean isCompleted;
	
	private String checkInDate;
	
	private String checkInTime;
	
	private Boolean isInCheckInTime;
	
	private String checkOutDate;
	
	private Date outDate;
	
	private String checkOutTime;
	
	private String bookingStatus;
	
	private OJAdress adress;
	
	private String bookingDate;
	
	private String airportName;
	
	private String timeZoneOffset;

	public String getBookingReference() {
		return bookingReference;
	}

	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public List<OJHotelDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<OJHotelDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public List<OJHotelAmenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<OJHotelAmenity> amenities) {
		this.amenities = amenities;
	}

	public OJHotelPosition getPosition() {
		return position;
	}

	public void setPosition(OJHotelPosition position) {
		this.position = position;
	}

	public List<OJHotelRoom> getRooms() {
		return rooms;
	}

	public void setRooms(List<OJHotelRoom> rooms) {
		this.rooms = rooms;
	}

	public Boolean isStayed() {
		return isStayed;
	}

	public void setIsStayed(Boolean isStayed) {
		this.isStayed = isStayed;
	}

	public Boolean isCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Boolean getIsInCheckInTime() {
		return isInCheckInTime;
	}

	public void setIsInCheckInTime(Boolean isInCheckInTime) {
		this.isInCheckInTime = isInCheckInTime;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public OJAdress getAdress() {
		return adress;
	}

	public void setAdress(OJAdress adress) {
		this.adress = adress;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
	
	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getWrapperHash() {
		return name + checkInDate + checkInTime + checkOutDate + checkOutTime;
	}
	
}
