package com.cathaypacific.mmbbizrule.cxservice.hzmredemtionbanner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.cxservice.hzmredemtionbanner.model.request.HZMBannerRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.hzmredemtionbanner.model.response.HZMBannerResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.hzmredemtionbanner.service.HZMBannerEligibleService;
import com.cathaypacific.mmbbizrule.util.HttpClientService;
import com.google.gson.Gson;

@Service
public class HZMBannerEligibleServiceImpl implements HZMBannerEligibleService{

	private static final Gson GSON = new Gson();
	private static LogAgent logger = LogAgent.getLogAgent(HZMBannerEligibleServiceImpl.class);
	

	@Value("${hzmBannerEligible.cxTransportationUrl}")
	private String cxLandTransportationUrl;
	
	
	@Autowired
	private HttpClientService httpClientService;
	
	
	
	@Override
	public HZMBannerResponseDTO checkHZMBannerEligible(String origin, String destination, String rbd, String iataNum, String creationDate, String carrierCode, String flightNum, String departureDate) {
		
		HZMBannerRequestDTO hzmBannerRequestDTO = new HZMBannerRequestDTO();
		
		
		hzmBannerRequestDTO.setOrigin(origin);
		hzmBannerRequestDTO.setDestination(destination);
		hzmBannerRequestDTO.setRbd(rbd);
		hzmBannerRequestDTO.setIataNum(iataNum);
		hzmBannerRequestDTO.setCreationDate(creationDate);
		hzmBannerRequestDTO.setCarrierCode(carrierCode);
		hzmBannerRequestDTO.setFlightNum(flightNum);
		hzmBannerRequestDTO.setDepartureDate(departureDate);
		
		
		try {
			String requestBody = GSON.toJson(hzmBannerRequestDTO);
			
			String responseBody =
					httpClientService.postJson(cxLandTransportationUrl, requestBody);
			
			HZMBannerResponseDTO hzmBannerResponseDTO = GSON.fromJson(responseBody, HZMBannerResponseDTO.class);

			return hzmBannerResponseDTO;
			
			
		} catch (Exception e) {
			String errorMsg= String.format("Unable to retrieve data from cx land tranportation API");
			logger.error(errorMsg);
		}
		
		
		return null;
	}

}
