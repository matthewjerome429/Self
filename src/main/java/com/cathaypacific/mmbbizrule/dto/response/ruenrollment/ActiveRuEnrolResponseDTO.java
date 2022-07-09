package com.cathaypacific.mmbbizrule.dto.response.ruenrollment;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class ActiveRuEnrolResponseDTO extends BaseResponseDTO{
	
	private static final long serialVersionUID = 1700899679435635960L;

	private Boolean success;
	
	private Boolean emailRegistered;
	
	private String email;
	
	private Boolean bookingAdded;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Boolean getEmailRegistered() {
		return emailRegistered;
	}

	public void setEmailRegistered(Boolean emailRegistered) {
		this.emailRegistered = emailRegistered;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getBookingAdded() {
		return bookingAdded;
	}

	public void setBookingAdded(Boolean bookingAdded) {
		this.bookingAdded = bookingAdded;
	}
}
