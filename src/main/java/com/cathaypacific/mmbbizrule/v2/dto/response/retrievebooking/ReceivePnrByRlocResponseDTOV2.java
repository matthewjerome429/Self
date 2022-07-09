package com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking;

import com.cathaypacific.mmbbizrule.v2.dto.common.booking.BookingDTOV2;

import io.swagger.annotations.ApiModelProperty;

public class ReceivePnrByRlocResponseDTOV2 extends RetrieveBookingBaseResponseDTOV2 {

	private static final long serialVersionUID = 2864929952836601630L;

	private BookingDTOV2 booking;
	
	private boolean loginSuccess;
	
	@ApiModelProperty("Vaild MMB TOKEN")
	private String mmbToken;
	
	private boolean requireLocalConsentPage = false;

	public boolean getRequireLocalConsentPage() {
		return requireLocalConsentPage;
	}

	public void setRequireLocalConsentPage(boolean requireLocalConsentPage) {
		this.requireLocalConsentPage = requireLocalConsentPage;
	}

	public BookingDTOV2 getBooking() {
		return booking;
	}

	public void setBooking(BookingDTOV2 booking) {
		this.booking = booking;
	}
	
	public boolean isLoginSuccess() {
		return loginSuccess;
	}

	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

	public String getMmbToken() {
		return mmbToken;
	}

	public void setMmbToken(String mmbToken) {
		this.mmbToken = mmbToken;
	}

}
