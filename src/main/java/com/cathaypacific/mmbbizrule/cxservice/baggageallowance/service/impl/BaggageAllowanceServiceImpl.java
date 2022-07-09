package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mmbbizrule.config.BaggageAllowanceConfig;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BgAlBtuRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od.BgAlOdRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BgAlBtuResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BtuResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdBookingResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.service.BaggageAllowanceService;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.util.BaggageAllowanceErrorUtil;
import com.cathaypacific.mmbbizrule.util.HttpClientService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class BaggageAllowanceServiceImpl implements BaggageAllowanceService {

	private static final String TRAVEL_SKY_NDC = "TSNDC";

	private static final Gson GSON = new Gson();
	
	@Autowired
	private BaggageAllowanceConfig baggageAllowanceConfig;

	@Autowired
	private HttpClientService httpClientService;

	@Override
	@LogPerformance(message="Time required to get baggage allowance by BTU from baggage allowance service.")
	public BgAlBtuResponseDTO getBaggageAllowanceByBtu(BgAlBtuRequestDTO requestDTO)
			throws BusinessBaseException {

		try {
			String requestBody = GSON.toJson(requestDTO);
			
			String responseBody =
					httpClientService.postJson(baggageAllowanceConfig.getBaggageAllowanceByBtuUrl(), requestBody);
			Type listType = new TypeToken<List<BtuResponseDTO>>() {
			}.getType();
			List<BtuResponseDTO> btuList = GSON.fromJson(responseBody, listType);
			
			BgAlBtuResponseDTO responseDTO = new BgAlBtuResponseDTO();
			responseDTO.setBtu(btuList);

			return responseDTO;
		} catch (Exception e) {
			throw new UnexpectedException("Get baggage allowance by BTU failed", BaggageAllowanceErrorUtil.parseError(e), e);
		}
	}
	
	@Override
	@LogPerformance(message="Time required to get baggage allowance by OD from baggage allowance service.")
	public BgAlOdResponseDTO getBaggageAllowanceByOd(BgAlOdRequestDTO requestDTO)
			throws BusinessBaseException {

		try {
			String requestBody = GSON.toJson(requestDTO);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set(MMBConstants.HEADER_KEY_ACCESS_CHANNEL, TRAVEL_SKY_NDC);
			
			String responseBody =
					httpClientService.postJson(baggageAllowanceConfig.getBaggageAllowanceByOdUrl(), requestBody, httpHeaders);
			Type listType = new TypeToken<List<BgAlOdBookingResponseDTO>>() {
			}.getType();
			List<BgAlOdBookingResponseDTO> bookingList = GSON.fromJson(responseBody, listType);
			
			BgAlOdResponseDTO responseDTO = new BgAlOdResponseDTO();
			responseDTO.setBookings(bookingList);

			return responseDTO;
		} catch (Exception e) {
			throw new UnexpectedException("Get baggage allowance by OD failed", BaggageAllowanceErrorUtil.parseError(e), e);
		}
	}

}
