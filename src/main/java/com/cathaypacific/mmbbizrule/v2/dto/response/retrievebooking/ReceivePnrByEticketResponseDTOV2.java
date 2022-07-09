package com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking;

import com.cathaypacific.mmbbizrule.v2.dto.common.booking.BookingDTOV2;

public class ReceivePnrByEticketResponseDTOV2 extends RetrieveBookingBaseResponseDTOV2 {

	private static final long serialVersionUID = 1671851018573930639L;

	private BookingDTOV2 booking;
	
	private boolean loginSuccess;

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
