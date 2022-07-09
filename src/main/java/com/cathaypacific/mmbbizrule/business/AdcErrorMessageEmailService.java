package com.cathaypacific.mmbbizrule.business;

import java.util.List;

import com.cathaypacific.mbcommon.model.common.AdcMessage;
import com.cathaypacific.mmbbizrule.dto.request.checkin.accept.CheckInAcceptRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;

public interface AdcErrorMessageEmailService {

	/**
	 * send adc error message Email
	 * @param adcMessageFromCache
	 * @param checkInAcceptResponse
	 * @param requestDTO
	 * @param bookingMergedCpr
	 */
	public void sendEmail(List<AdcMessage> adcMessageFromCache, CheckInAcceptResponseDTO checkInAcceptResponse,
			CheckInAcceptRequestDTO requestDTO, Booking bookingMergedCpr);

}
