package com.cathaypacific.mmbbizrule.dto.response.meal.updateMeal;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;

public class UpdateMealResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 4889382003606770550L;
	
	private boolean isSuccess;
	
	private FlightBookingDTO booking;
	
	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public FlightBookingDTO getBooking() {
		return booking;
	}

	public void setBooking(FlightBookingDTO booking) {
		this.booking = booking;
	}
	
	
}
