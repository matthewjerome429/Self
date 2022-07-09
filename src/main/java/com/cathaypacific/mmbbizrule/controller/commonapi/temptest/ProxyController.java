package com.cathaypacific.mmbbizrule.controller.commonapi.temptest;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.Api;

/* REMOVE TEST CODE BEFORE GO LIVE R5.0
@Api(tags = "Proxy For Internal Service", description = "just provide service for local debug, will delete before go live")
@RestController
@RequestMapping(path = "/proxy")
*/
public class ProxyController {

	private static final LogAgent LOGGER = LogAgent.getLogAgent(ProxyController.class);
	
	private static final JsonParser PARSER = new JsonParser();
	
	@Value("${cx.memberprofile.endpoint.path.details}")
	private String endpointPathDetails;
	
	@Value("${cx.memberprofile.endpoint.path.details.v2}")
	private String endpointPathDetailsV2;

	@Value("${member.services.api.key}")
	private String apiKey;
	
	@Value("${member.services.api.key.v2}")
	private String apiKeyV2;

	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping(path = "/member/v1/**")
	public String invokeMemberServiceV1(@RequestBody String requestBody, @RequestHeader Map<String, String> requestHeader,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		String requestURI = httpRequest.getRequestURI();
		String relativeURI = requestURI.substring("/proxy/member/v1".length());
		String serviceURI = getMemberServiceEndpointV1() + relativeURI;
		
		LOGGER.info("Proxy for member service V1: " + serviceURI);
		
		URI uri = UriComponentsBuilder.fromHttpUrl(serviceURI).build().toUri();
		
		RequestEntity<String> requestEntity =
				RequestEntity.post(uri).header("apiKey", apiKey).header("Content-Type", "application/json").body(requestBody);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		
		httpResponse.setStatus(responseEntity.getStatusCode().value());
		return responseEntity.getBody();
	}
	
	@PostMapping(path = "/member/v2/**")
	public String invokeMemberServiceV2(@RequestBody String requestBody, @RequestHeader Map<String, String> requestHeader,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		
		JsonObject jsonObject = PARSER.parse(requestBody).getAsJsonObject();
		
		String requestURI = httpRequest.getRequestURI();
		String relativeURI = requestURI.substring("/proxy/member/v2".length());
		String serviceURI = getMemberServiceEndpointV2() + relativeURI;
		
		LOGGER.info("Proxy for member service V2: " + serviceURI);
		
		URI uri = UriComponentsBuilder.fromHttpUrl(serviceURI).build().toUri();
		
		RequestEntity<String> requestEntity = RequestEntity.post(uri)
					.header("X-Consumer-ID", MMBUtil.getCurrentAppCode())
					.header("X-Consumer-Username", MMBUtil.getCurrentAppCode())
					.header("Content-Type",	"application/json")
					.header("apiKey", apiKeyV2)
					.header("trace_id",	jsonObject.get("correlationId").getAsString())
					.body(requestBody);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		
		httpResponse.setStatus(responseEntity.getStatusCode().value());
		return responseEntity.getBody();
	}

	private String getMemberServiceEndpointV1() {
		int resourceIndex = this.endpointPathDetails.indexOf("/amcls-cls-mem-creation/v2.0");
		return endpointPathDetails.substring(0, resourceIndex);
	}
	
	private String getMemberServiceEndpointV2() {
		int resourceIndex = this.endpointPathDetailsV2.indexOf("/amcls-cls-member-profile/v2.0");
		return endpointPathDetails.substring(0, resourceIndex);
	}
	
}
