package com.cathaypacific.mmbbizrule.dto.request.bookingcancel;

import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class BookingCancelRequestDTO {

	@Pattern(regexp = "^[A-Za-z0-9]{6,7}$",message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String rloc;
	
	private String languageLocale;
	
	private boolean refund;

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getLanguageLocale() {
		return languageLocale;
	}

	public void setLanguageLocale(String languageLocale) {
		this.languageLocale = languageLocale;
	}

	public boolean isRefund() {
		return refund;
	}

	public void setRefund(boolean refund) {
		this.refund = refund;
	}
	
}
