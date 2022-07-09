package com.cathaypacific.mmbbizrule.cxservice.ruenrollment.service.impl;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.request.RuEnrolRequest;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.response.RuEnrolResponse;
import com.cathaypacific.mmbbizrule.cxservice.ruenrollment.service.RuEnrollmentService;

@Service
public class RuEnrollmentServiceImpl implements RuEnrollmentService{
	
	@Value("${cx.ruenrollment.endpoint.path.activeruenrol}")
	private String endpointActiveRuEnrol;
	
	@Value("${member.services.api.key.v2}")
	private String apiKeyV2;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public RuEnrolResponse enrolActiveRuAccount(RuEnrolRequest request, String appCode) {
		URI uri = UriComponentsBuilder.fromHttpUrl(endpointActiveRuEnrol).build().toUri();

		RequestEntity<RuEnrolRequest> requestEntity = RequestEntity.post(uri).header("Content-Type", "application/json")
				.header("apiKey", apiKeyV2).header("trace_id", request.getCorrelationId()).body(request);

		ResponseEntity<RuEnrolResponse> responseEntity = restTemplate.exchange(requestEntity, RuEnrolResponse.class);
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			return responseEntity.getBody();
		} else {
			return null;
		}
	}
}
