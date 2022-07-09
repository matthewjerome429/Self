package com.cathaypacific.mmbbizrule.dto.request.rebooking;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.annotation.IsValidDate;
import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;

public class AcceptFlightInfoDTO {
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String marketCarrierCode;
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String marketFlightNumber;
	
	@IsValidDate(nullable = false, dateFormat = RetrievePnrDepartureArrivalTime.TIME_FORMAT)
	private String scheduledTime;

	public String getMarketCarrierCode() {
		return marketCarrierCode;
	}

	public void setMarketCarrierCode(String marketCarrierCode) {
		this.marketCarrierCode = marketCarrierCode;
	}

	public String getMarketFlightNumber() {
		return marketFlightNumber;
	}

	public void setMarketFlightNumber(String marketFlightNumber) {
		this.marketFlightNumber = marketFlightNumber;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	
}
