package com.cathaypacific.mmbbizrule.cxservice.plusgrade.model;

import java.io.Serializable;

public class PlusgradeRequestFlightDTO implements Serializable {

	private static final long serialVersionUID = 2229299869388542470L;
	
	private String origination;
	private String destination;
	private String carrierCode;
	private String flightNumber;
	private String departureDate;
	private String departureTime;
	private String fareClass;
	
	public String getOrigination() {
		return origination;
	}
	public void setOrigination(String origination) {
		this.origination = origination;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getCarrierCode() {
		return carrierCode;
	}
	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public String getFareClass() {
		return fareClass;
	}
	public void setFareClass(String fareClass) {
		this.fareClass = fareClass;
	}
	
	@Override
	public String toString() {
		return "PlusgradeRequestFlightDTO [origination=" + origination + ", destination=" + destination
				+ ", carrierCode=" + carrierCode + ", flightNumber=" + flightNumber + ", departureDate=" + departureDate
				+ ", departureTime=" + departureTime + ", fareClass=" + fareClass + "]";
	}	
}
