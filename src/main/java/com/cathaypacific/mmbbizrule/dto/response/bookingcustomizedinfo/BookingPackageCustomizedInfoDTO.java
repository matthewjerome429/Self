package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.io.Serializable;

public class BookingPackageCustomizedInfoDTO implements Serializable{
	
	private static final long serialVersionUID = 5623930748692801405L;

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
