package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.additional;

import java.io.Serializable;

public class PassengerSegmentAdditionalInfoDTO implements Serializable{
	
	private static final long serialVersionUID = -3975605477604381130L;
	
	private String segmentId;
	
	private String passengerId;
	
    private boolean exlBannerEligible;
    
	private boolean asrBannerEligible;
	    
	private boolean baggageBannerEligible;
	
    private Boolean seatProductAvailable;

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public boolean isExlBannerEligible() {
		return exlBannerEligible;
	}

	public void setExlBannerEligible(boolean exlBannerEligible) {
		this.exlBannerEligible = exlBannerEligible;
	}

	public boolean isAsrBannerEligible() {
		return asrBannerEligible;
	}

	public void setAsrBannerEligible(boolean asrBannerEligible) {
		this.asrBannerEligible = asrBannerEligible;
	}

	public boolean isBaggageBannerEligible() {
		return baggageBannerEligible;
	}

	public void setBaggageBannerEligible(boolean baggageBannerEligible) {
		this.baggageBannerEligible = baggageBannerEligible;
	}

	public Boolean getSeatProductAvailable() {
		return seatProductAvailable;
	}

	public void setSeatProductAvailable(Boolean seatProductAvailable) {
		this.seatProductAvailable = seatProductAvailable;
	}

}
