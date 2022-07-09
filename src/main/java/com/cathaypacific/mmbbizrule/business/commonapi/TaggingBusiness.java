package com.cathaypacific.mmbbizrule.business.commonapi;

import com.cathaypacific.mmbbizrule.dto.response.tagging.TaggingDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;

public interface TaggingBusiness {

	public TaggingDTO retrieveTagMapping();
	
	public boolean checkRedemptionBannerUpgrade(Booking booking);
	
}
