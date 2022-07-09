package com.cathaypacific.mmbbizrule.dto.request.bookingproperties.additional;

public class BookingAdditionalInfoRequestDTO {
	/**
	 * default false
	 */
	private boolean plusgrade = false;
	
	private boolean ancillaryBaggageBanner = false;
	
	private boolean ancillaryEXLSeatBanner = false;
	
	private boolean ancillaryASRSeatBanner = false;
	
	private boolean ancillaryLoungeBanner = false;
	
	private boolean seatProductAvailableCheck = false;

	public boolean isPlusgrade() {
		return plusgrade;
	}

	public void setPlusgrade(boolean plusgrade) {
		this.plusgrade = plusgrade;
	}


	public boolean isAncillaryBaggageBanner() {
		return ancillaryBaggageBanner;
	}

	public void setAncillaryBaggageBanner(boolean ancillaryBaggageBanner) {
		this.ancillaryBaggageBanner = ancillaryBaggageBanner;
	}

	public boolean isAncillaryEXLSeatBanner() {
		return ancillaryEXLSeatBanner;
	}

	public void setAncillaryEXLSeatBanner(boolean ancillaryEXLSeatBanner) {
		this.ancillaryEXLSeatBanner = ancillaryEXLSeatBanner;
	}

	public boolean isAncillaryASRSeatBanner() {
		return ancillaryASRSeatBanner;
	}

	public void setAncillaryASRSeatBanner(boolean ancillaryASRSeatBanner) {
		this.ancillaryASRSeatBanner = ancillaryASRSeatBanner;
	}

	public boolean isAncillaryLoungeBanner() {
		return ancillaryLoungeBanner;
	}

	public void setAncillaryLoungeBanner(boolean ancillaryLoungeBanner) {
		this.ancillaryLoungeBanner = ancillaryLoungeBanner;
	}

	public boolean isSeatProductAvailableCheck() {
		return seatProductAvailableCheck;
	}

	public void setSeatProductAvailableCheck(boolean seatProductAvailableCheck) {
		this.seatProductAvailableCheck = seatProductAvailableCheck;
	}

}
