package com.cathaypacific.mmbbizrule.cxservice.aep.service;

import java.util.concurrent.Future;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;

public interface AEPService {

	public Future<AEPProductsResponse> asyncGetBookingProducts(String bookingRef, String pos) throws BusinessBaseException;
	
	/**
	 * Get product by booking ref and pos
	 * @param bookingRef
	 * @param pos
	 * @return
	 * @throws BusinessBaseException
	 */
	public AEPProductsResponse getBookingProducts(String bookingRef, String pos) throws BusinessBaseException;
	
	/**
	 * Get product for baggage call
	 * @param pnrBooking
	 * @return
	 * @throws BusinessBaseException
	 */
	public AEPProductsResponse getBookingProductsForBaggage(Booking booking) throws BusinessBaseException;

}
