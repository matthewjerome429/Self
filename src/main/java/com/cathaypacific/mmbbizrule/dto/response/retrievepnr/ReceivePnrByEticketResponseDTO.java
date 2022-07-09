package com.cathaypacific.mmbbizrule.dto.response.retrievepnr;

import com.cathaypacific.mmbbizrule.dto.common.booking.BookingDTO;

public class ReceivePnrByEticketResponseDTO extends RetrieveBookingBaseResponseDTO {

	private static final long serialVersionUID = 1671851018573930639L;

	private BookingDTO booking;
	
	private boolean loginSuccess;

	private String mmbToken;
	
	private boolean requireLocalConsentPage = false;
	
	public boolean getRequireLocalConsentPage() {
		return requireLocalConsentPage;
	}

	public void setRequireLocalConsentPage(boolean requireLocalConsentPage) {
		this.requireLocalConsentPage = requireLocalConsentPage;
	}

	public BookingDTO getBooking() {
		return booking;
	}

	public void setBooking(BookingDTO booking) {
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
