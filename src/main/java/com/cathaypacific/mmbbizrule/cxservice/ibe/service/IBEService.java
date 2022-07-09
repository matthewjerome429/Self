package com.cathaypacific.mmbbizrule.cxservice.ibe.service;

import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.request.CancelBookingEligibilityRequest;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.response.CancelBookingEligibilityResponse;
import com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request.TicketRefundRequestDTO;

public interface IBEService {
	
	/**
	 * Ticket refund through IBE
	 * 
	 * @param request
	 * @return
	 * @throws UnexpectedException
	 */
	public boolean submitTicketRefund(TicketRefundRequestDTO request) throws UnexpectedException;
	
	/**
	 * 
	 * @return
	 */
	public CancelBookingEligibilityResponse checkCancelBookingEligibility(CancelBookingEligibilityRequest cancelBookingEligibilityRequest);
	
}
