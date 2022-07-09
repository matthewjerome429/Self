package com.cathaypacific.mmbbizrule.cxservice.eods.service;

import java.util.List;

import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.cxservice.eods.model.EODSBooking;

public interface EODSBookingService {
	/**
	 * Get Booking List from EODS
	 * @param memebrId
	 * @return
	 */
	public List<EODSBooking> getBookingList(String memebrId) throws UnexpectedException;
}
