package com.cathaypacific.mmbbizrule.model.booking.summary;

import java.util.Date;

public class HotelSummary extends SectorSummaryBase {

	private static final long serialVersionUID = -7057738182145849597L;
	
	private String hotelName;
	
	private String cityName;
	
	private String timeZoneOffset;
	
	private String bookingDate;
	
	private String checkInDate;
	
	private String checkOutDate;
	
	private String checkOutTime;
	
	private String nightOfStay;
	
	private String hotelStatus;
	
	private Date checkOutGMTDate;

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getNightOfStay() {
		return nightOfStay;
	}

	public void setNightOfStay(String nightOfStay) {
		this.nightOfStay = nightOfStay;
	}

	public String getHotelStatus() {
		return hotelStatus;
	}

	public void setHotelStatus(String hotelStatus) {
		this.hotelStatus = hotelStatus;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
	
	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public Date getCheckOutGMTDate() {
		return checkOutGMTDate;
	}

	public void setCheckOutGMTDate(Date checkOutGMTDate) {
		this.checkOutGMTDate = checkOutGMTDate;
	}
	
}
