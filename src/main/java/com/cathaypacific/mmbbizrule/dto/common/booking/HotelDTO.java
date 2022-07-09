package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class HotelDTO extends BookingOrderDTO implements Serializable{
	
	private static final long serialVersionUID = 3175202352536304320L;

	private String bookingReference;
	
	private String name;
	
	private String urlPath;
	
	private List<HotelDescriptionDTO> descriptions;
	
	private List<HotelAmenityDTO> amenities;
	
	private HotelPositionDTO position;
	
	private List<HotelRoomDTO> rooms;
	
	private Boolean isStayed;
	
	private Boolean isCompleted;
	
	private String checkInDate;
	
	private String checkInTime;
	
	private Boolean isInCheckInTime;
	
	private String checkOutDate;
	
	@JsonIgnore
	private Date outDate;
	
	private String checkOutTime;
	
	private String bookingStatus;
	
	private AdressDTO adress;
	
	private String bookingDate;

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

	public List<HotelDescriptionDTO> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<HotelDescriptionDTO> descriptions) {
		this.descriptions = descriptions;
	}

	public List<HotelAmenityDTO> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<HotelAmenityDTO> amenities) {
		this.amenities = amenities;
	}

	public HotelPositionDTO getPosition() {
		return position;
	}

	public void setPosition(HotelPositionDTO position) {
		this.position = position;
	}

	public List<HotelRoomDTO> getRooms() {
		return rooms;
	}

	public void setRooms(List<HotelRoomDTO> rooms) {
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

	public AdressDTO getAdress() {
		return adress;
	}

	public void setAdress(AdressDTO adress) {
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
	
	
}
