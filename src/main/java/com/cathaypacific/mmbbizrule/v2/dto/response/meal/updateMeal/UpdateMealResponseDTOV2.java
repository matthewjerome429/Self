package com.cathaypacific.mmbbizrule.v2.dto.response.meal.updateMeal;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;

public class UpdateMealResponseDTOV2 extends BaseResponseDTO {

	private static final long serialVersionUID = 4889382003606770550L;
	
	private boolean isSuccess;
	
	private FlightBookingDTOV2 booking;
	
	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public FlightBookingDTOV2 getBooking() {
		return booking;
	}

	public void setBooking(FlightBookingDTOV2 booking) {
		this.booking = booking;
	}
	
	
}
