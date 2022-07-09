package com.cathaypacific.mmbbizrule.cxservice.hzmredemtionbanner.service;

import com.cathaypacific.mmbbizrule.cxservice.hzmredemtionbanner.model.response.HZMBannerResponseDTO;

public interface HZMBannerEligibleService {
	HZMBannerResponseDTO checkHZMBannerEligible(String origin, String destination, String rbd, String officeId, String creationDate, String carrierCode, String flightNum, String departureDate);
}
