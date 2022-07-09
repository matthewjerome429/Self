package com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.service;

import com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.model.request.RescheduleEligibleRequest;
import com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.model.response.RescheduleEligibleResponse;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to Reschedule for Booking
 * @author fangfang.zhang
 * @date Feb 16, 2019 5:19:37 PM
 * @version V1.0
 */
public interface DpAtciRebookService {
	
	/**
	 * 
	 * @Description update seat
	 * @param
	 * @return RetrievePnrBooking
	 * @author fangfang.zhang
	 * @param map 
	 */
	public RescheduleEligibleResponse getBookingReschedule(RescheduleEligibleRequest requestDTO);
	
}
