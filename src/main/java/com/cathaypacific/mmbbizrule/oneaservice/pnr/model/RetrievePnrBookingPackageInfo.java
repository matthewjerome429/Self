package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RetrievePnrBookingPackageInfo {
	
	/** spnr in SK freeText */
	private String spnr;
	
	/** mobile booking */
	private boolean mobBooking;
	
	/** mobile booking */
	private boolean ndcBooking;
	
	/** has hotel */
	private boolean hasHotel;
	
	/** has event*/
	private boolean hasEvent;
	
	/** has flight*/
	private boolean hasFlight;
	
	public String getSpnr() {
		return spnr;
	}

	public void setSpnr(String spnr) {
		this.spnr = spnr;
	}

	@JsonIgnore
	public boolean isFlightOnly(){
		return hasFlight&&!hasEvent&&!hasHotel;
	}
	
	@JsonIgnore
	public boolean isPackageBooking() {
		return (hasFlight && (hasHotel || hasEvent)) || (!hasFlight && (hasEvent && hasHotel));
	}
	
	public boolean isMobBooking() {
		return mobBooking;
	}

	public void setMobBooking(boolean mobBooking) {
		this.mobBooking = mobBooking;
	}

	public boolean isNdcBooking() {
		return ndcBooking;
	}

	public void setNdcBooking(boolean ndcBooking) {
		this.ndcBooking = ndcBooking;
	}

	public void setHasHotel(boolean hasHotel) {
		this.hasHotel = hasHotel;
	}

	public void setHasEvent(boolean hasEvent) {
		this.hasEvent = hasEvent;
	}

	public void setHasFlight(boolean hasFlight) {
		this.hasFlight = hasFlight;
	}
	
	public boolean isHasHotel() {
		return hasHotel;
	}
	
	public boolean isHasEvent() {
		return hasEvent;
	}
	
	public boolean isHasFlight() {
		return hasFlight;
	}
	
}
