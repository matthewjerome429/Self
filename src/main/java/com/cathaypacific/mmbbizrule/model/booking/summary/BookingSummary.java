package com.cathaypacific.mmbbizrule.model.booking.summary;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class BookingSummary {

	//private String rloc;
	
	private String spnr;
	
	private String bookingType;
	
	private String bookingStatus;

	private FlightBookingSummary flightSummary;
	
	private HotelBookingSummary hotelSummary;
	
	private EventBookingSummary eventSummary;
	
	private Date firstAvlSectorDate;
	
//	public String getOjRloc() {
//		if(flightSummary != null) {
//			return flightSummary.getSpnr();
//		} else {
//			return rloc;
//		}
//	}

	public String getOneARloc() {
		if(flightSummary != null) {
			return flightSummary.getOneARloc();
		} else {
			return null;
		}
	}
	
//	public String getRloc() {
//		return rloc;
//	}
//
//	public void setRloc(String rloc) {
//		this.rloc = rloc;
//	}
	
	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public FlightBookingSummary getFlightSummary() {
		return flightSummary;
	}

	public void setFlightSummary(FlightBookingSummary flightSummary) {
		this.flightSummary = flightSummary;
	}

	public HotelBookingSummary getHotelSummary() {
		return hotelSummary;
	}

	public void setHotelSummary(HotelBookingSummary hotelSummary) {
		this.hotelSummary = hotelSummary;
	}

	public EventBookingSummary getEventSummary() {
		return eventSummary;
	}

	public void setEventSummary(EventBookingSummary eventSummary) {
		this.eventSummary = eventSummary;
	}

	public Date getFirstAvlSectorDate() {
		return firstAvlSectorDate;
	}

	public void setFirstAvlSectorDate(Date firstAvlSectorDate) {
		this.firstAvlSectorDate = firstAvlSectorDate;
	}

	public String getSpnr() {
		if(StringUtils.isNotEmpty(spnr)){
			return spnr;
		}else if(flightSummary!=null){
			return flightSummary.getSpnr();
		}else{
			return null;
		}
	}
	
	public String getDisplayRloc() {
		return StringUtils.defaultString(getSpnr(), getOneARloc());
	}

	public void setSpnr(String spnr) {
		this.spnr = spnr;
	}

}
