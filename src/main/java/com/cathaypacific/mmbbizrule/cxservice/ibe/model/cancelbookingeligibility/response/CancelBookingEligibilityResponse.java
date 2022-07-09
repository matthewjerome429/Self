package com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.response;

import java.util.List;

public class CancelBookingEligibilityResponse {

	/**
	 * True = Booking eligible for ATC cancel+refund
	 */
	private boolean flag;
	
	/**
	 * error info of IBE
	 */
	private List<CancelBookingEligibilityError> errors;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public List<CancelBookingEligibilityError> getErrors() {
		return errors;
	}

	public void setErrors(List<CancelBookingEligibilityError> errors) {
		this.errors = errors;
	}
	
}
