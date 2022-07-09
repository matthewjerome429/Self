package com.cathaypacific.mmbbizrule.v2.dto.response.bookingsummary;

public class HotelSummaryDTOV2 extends SummaryDTOV2{
	
	private static final long serialVersionUID = 6757307042990541239L;

	private String hotelName;
	
	private String cityName;
	
	private String timeZone;
	
	private String checkInDate;
	
	private String checkOutDate;
	
	private String bookingDate;
	
	private String nightOfStay;
	
	private String hotelStatus;

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

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
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

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
	
	
	
}