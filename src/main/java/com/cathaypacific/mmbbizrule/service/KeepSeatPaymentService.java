package com.cathaypacific.mmbbizrule.service;

import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;

public interface KeepSeatPaymentService {
	/**
	 * 
	* @Description populate seat payment info from booking before update to booking after update
	* @param bookingBefore
	* @param bookingAfter
	* @return RetrievePnrBooking
	* @author haiwei.jia
	 */
	public Booking keepSeatPayment(Booking bookingBefore, Booking bookingAfter);
}
