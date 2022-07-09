package com.cathaypacific.mmbbizrule.model.booking.detail;

public class ContactMobile {
	
	/** The mobile number of passenger level: mobileNumber. (removed country number) */ 
	private String mobileNumber;

	/** The country number of passenger level, split from mobileNumber */
	private String mobileCountryNumber;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMobileCountryNumber() {
		return mobileCountryNumber;
	}

	public void setMobileCountryNumber(String mobileCountryNumber) {
		this.mobileCountryNumber = mobileCountryNumber;
	}
	
}
