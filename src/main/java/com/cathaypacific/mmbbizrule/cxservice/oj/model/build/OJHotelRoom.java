package com.cathaypacific.mmbbizrule.cxservice.oj.model.build;

import java.util.List;

public class OJHotelRoom {

	private String duration;
	
	private String checkInDate;
	
	private String checkInDay;
	
	private String checkInTime;
	
	private String checkOutDate;
	
	private String checkOutDay;
	
	private String checkOutTime;
	
	private List<OJHotelRoomOption> roomOptions;
	
	private String adults;
	
	private String infants;
	
	private String children;
	
	private Boolean isStayed;
	
	private Boolean isCompleted;
	
	private String bookingReference;
	
	private String bookingStatus;

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getCheckInDay() {
		return checkInDay;
	}

	public void setCheckInDay(String checkInDay) {
		this.checkInDay = checkInDay;
	}

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getCheckOutDay() {
		return checkOutDay;
	}

	public void setCheckOutDay(String checkOutDay) {
		this.checkOutDay = checkOutDay;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public List<OJHotelRoomOption> getRoomOptions() {
		return roomOptions;
	}

	public void setRoomOptions(List<OJHotelRoomOption> roomOptions) {
		this.roomOptions = roomOptions;
	}

	public String getAdults() {
		return adults;
	}

	public void setAdults(String adults) {
		this.adults = adults;
	}

	public String getInfants() {
		return infants;
	}

	public void setInfants(String infants) {
		this.infants = infants;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
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

	public String getBookingReference() {
		return bookingReference;
	}

	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	
}