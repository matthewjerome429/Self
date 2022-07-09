package com.cathaypacific.mmbbizrule.cxservice.sentemail.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mmbbizrule.cxservice.sentemail.service.FIFBelowService;
import com.cathaypacific.mmbbizrule.dto.request.email.FIFBelowRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.email.FIFBelowReponseDTO;
import com.google.gson.Gson;

@Service
public class FIFBelowServiceImpl implements FIFBelowService {

	@Value("${endpoint.svcpax.sentEmail}")
	private String fifBlowEndPoint;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public FIFBelowReponseDTO sentEmail(FIFBelowRequestDTO request) {
		HttpHeaders requestHeaders = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		requestHeaders.setContentType(type);

		HttpEntity<String> requestEntity = new HttpEntity<>(new Gson().toJson(request), requestHeaders);

		return restTemplate.postForObject(fifBlowEndPoint, requestEntity, FIFBelowReponseDTO.class);

	}

}
