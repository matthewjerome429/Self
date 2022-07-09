package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.additional;

import java.io.Serializable;

public class SegmentAdditionalInfoDTO implements Serializable{

	private static final long serialVersionUID = -3975605477604381130L;
	
	private String segmentId;
	
	
	/** Ancillary Banner Upgrade */
	private Boolean redemptionBannerEligible;
	
	
	/** eligible to apply for upgrade bid */
	private Boolean upgradeBidEligible;
	
	public Boolean getUpgradeBidEligible() {
		return upgradeBidEligible;
	}
	public void setUpgradeBidEligible(Boolean upgradeBidEligible) {
		this.upgradeBidEligible = upgradeBidEligible;
	}

	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	public Boolean isRedemptionBannerEligible() {
		return redemptionBannerEligible;
	}

	public void setRedemptionBannerEligible(Boolean redemptionBannerEligible) {
		this.redemptionBannerEligible = redemptionBannerEligible;
	}
}
