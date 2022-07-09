package com.cathaypacific.mmbbizrule.cxservice.memberaward.model.request;

public class SectorDetailRecordInRequest {
	private String bookingClass;
	
	private String carrierCode;
	
	private String destination;
	
	private String fareClassGroup;
	
	private String flightDate;
	
	private String flightNum;
	
	private String origin;

	private String tier;

	public String getBookingClass() {
		return bookingClass;
	}

	public void setBookingClass(String bookingClass) {
		this.bookingClass = bookingClass;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getFareClassGroup() {
		return fareClassGroup;
	}

	public void setFareClassGroup(String fareClassGroup) {
		this.fareClassGroup = fareClassGroup;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}

	public String getFlightNum() {
		return flightNum;
	}

	public void setFlightNum(String flightNum) {
		this.flightNum = flightNum;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}
	
}
