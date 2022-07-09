package com.cathaypacific.mmbbizrule.cxservice.seatmap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mmbbizrule.cxservice.seatmap.service.SeatMapService;
import com.cathaypacific.mmbbizrule.dto.request.seatmap.SeatMapRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.RetrieveSeatMapDTO;
import com.google.gson.Gson;

@Service
public class SeatMapServiceImpl implements SeatMapService {
	
	@Value("${endpoint.pnr.retrieveSeatMap}")
	private String seatMapEndPoint;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public RetrieveSeatMapDTO retrieveSeatMap(String mmbToken, SeatMapRequestDTO request) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("MMB-Token", mmbToken);
		requestHeaders.add("Cacheable", "true");
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        requestHeaders.setContentType(type);
        
        HttpEntity<String> requestEntity = new HttpEntity<>(new Gson().toJson(request), requestHeaders);

		return restTemplate.postForObject(seatMapEndPoint, requestEntity, RetrieveSeatMapDTO.class);
	}

}
