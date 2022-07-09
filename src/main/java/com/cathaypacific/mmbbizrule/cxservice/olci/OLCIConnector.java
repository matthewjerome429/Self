package com.cathaypacific.mmbbizrule.cxservice.olci;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.config.OLCIConfig;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.RetrieveJourneyResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.request.CancelCheckInRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.response.CancelCheckInResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.response.NonMemberLoginResponseDTO;
import com.cathaypacific.mmbbizrule.util.HttpClientService;
import com.cathaypacific.mmbbizrule.util.HttpResponse;
import com.google.gson.Gson;

@Component
@Deprecated// please use oneA consumer to invoke OLCI
public class OLCIConnector {

	@Autowired
	private OLCIConfig olciConfig;
	
	@Autowired
	private HttpClientService httpClientService;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@LogPerformance(message = "Time required to get check-in info")
	public HttpResponse<RetrieveJourneyResponseDTO> getJourneyResponseByRLOC(String rloc, String givenName, String familyName) {
		Map<String, String> params = new LinkedHashMap<>();
		params.put("rloc", rloc);
		params.put("givenName", givenName);
		params.put("familyName", familyName);
		String url = olciConfig.getJourneyResponseByRLOC();
		Map<String, String> hederParams = new HashMap<>();
		hederParams.put("Access-Channel", MMBConstants.APP_CODE);
		return httpClientService.getForObject(RetrieveJourneyResponseDTO.class, url, hederParams, params);
	}

	public ResponseEntity<NonMemberLoginResponseDTO>  nonMemberLoginWithOLCI(String rloc, String givenName, String familyName){
		Map<String, String> params = new LinkedHashMap<>();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(olciConfig.getNonMemberLogin())
				.queryParam("txtBookingRef", rloc).queryParam("txtGivenName", givenName)
				.queryParam("txtFamilyName", familyName);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Access-Channel", MMBConstants.APP_CODE);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<NonMemberLoginResponseDTO> responseEntity= httpClientService.getRestTemplate().exchange(builder.build().toString(),HttpMethod.GET, entity,NonMemberLoginResponseDTO.class,params);
		return responseEntity;
	}

	public CancelCheckInResponseDTO cancelCheckInWithOLCI(CancelCheckInRequestDTO cancelCheckInRequestDTO,String cookie){
		Gson gson = new Gson();
		String body = gson.toJson(cancelCheckInRequestDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Cookie",cookie);
		HttpEntity<String> entity = new HttpEntity<>(body,headers);
		String url = olciConfig.getCancelCheckIn();
		ResponseEntity<CancelCheckInResponseDTO> response  = httpClientService.getRestTemplate().exchange(url, HttpMethod.PUT, entity,CancelCheckInResponseDTO.class);
		return  response.getBody();
	}
	
}
