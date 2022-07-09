package com.cathaypacific.mmbbizrule.cxservice.ibe.model.cancelbookingeligibility.response;

public class CancelBookingEligibilityError {

	/**
	 * Error code 
	 */
	private String code;
	
	/**
	 * error message 
	 */
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
