package com.cathaypacific.mmbbizrule.v2.dto.response.updateseat;

import java.io.Serializable;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;

public class UpdateSeatResponseDTOV2 extends BaseResponseDTO implements Serializable{
	private static final long serialVersionUID = 1190850776755371403L;
	
	private FlightBookingDTOV2 booking;
	
	private boolean resendBPRequired;
	
	private Boolean resendBPSuccess;

	public FlightBookingDTOV2 getBooking() {
		return booking;
	}

	public void setBooking(FlightBookingDTOV2 booking) {
		this.booking = booking;
	}

	public boolean isResendBPRequired() {
		return resendBPRequired;
	}

	public void setResendBPRequired(boolean resendBPRequired) {
		this.resendBPRequired = resendBPRequired;
	}

	public Boolean getResendBPSuccess() {
		return resendBPSuccess;
	}

	public void setResendBPSuccess(Boolean resendBPSuccess) {
		this.resendBPSuccess = resendBPSuccess;
	}

}
