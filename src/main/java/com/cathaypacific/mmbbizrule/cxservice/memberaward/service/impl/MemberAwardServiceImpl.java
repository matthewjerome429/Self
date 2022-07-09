package com.cathaypacific.mmbbizrule.cxservice.memberaward.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mbcommon.aop.tokenlevelcache.TokenLevelCacheable;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mmbbizrule.config.MemberAwardConfig;
import com.cathaypacific.mmbbizrule.controller.RetrievePnrController;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.request.MemberAwardRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.model.response.MemberAwardResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberaward.service.MemberAwardService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class MemberAwardServiceImpl implements MemberAwardService {
	private static LogAgent logger = LogAgent.getLogAgent(RetrievePnrController.class);
	
	private Gson gson = new GsonBuilder().serializeNulls().create();
	
	@Autowired
	private MemberAwardConfig memberAwardConfig;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	@LogPerformance(message = "Time required to get asia miles info.")
	@TokenLevelCacheable(name=TokenCacheKeyEnum.ASIA_MILES)
	public MemberAwardResponse getMemberAward(MemberAwardRequest request) {
		request.setApplicationName(memberAwardConfig.getAppName());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("apiKey", memberAwardConfig.getApiKey());
		httpHeaders.set("Content-Type", "application/json");
		MemberAwardResponse response = null;
		try {
			logger.debug("MemberAward url test, url:"+memberAwardConfig.getMemberAwardUrl());
			HttpEntity<String> headers = new HttpEntity<>(gson.toJson(request), httpHeaders);
			ResponseEntity<MemberAwardResponse> responseEntity = restTemplate.exchange(memberAwardConfig.getMemberAwardUrl(), HttpMethod.POST, headers, MemberAwardResponse.class);
			if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
				response = responseEntity.getBody();
			}
		} catch (RestClientException e) {
			logger.error("Exception occurs when getMemberAward for member:"+request.getMemberNumber(), e);
		} catch (IllegalArgumentException e) {
			// This may happen when response status code is not supported by the library. E.g. 550 status code
			logger.error("Exception occurs when getMemberAward for member:"+request.getMemberNumber(), e);
		} catch (Exception e) {
			// Any exception should not crash the whole system
			logger.error("Exception occurs when getMemberAward for member:"+request.getMemberNumber(), e);
		}

		if(response == null){
			logger.warn("Can not get member award for member "+request.getMemberNumber());
		}
		return response;
	}
}
