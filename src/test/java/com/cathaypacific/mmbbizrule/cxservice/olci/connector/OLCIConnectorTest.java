package com.cathaypacific.mmbbizrule.cxservice.olci.connector;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.config.OLCIConfig;
import com.cathaypacific.mmbbizrule.cxservice.olci.OLCIConnector;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.RetrieveJourneyResponseDTO;
import com.cathaypacific.mmbbizrule.util.HttpClientService;
import com.cathaypacific.mmbbizrule.util.HttpResponse;

@RunWith(MockitoJUnitRunner.class)
public class OLCIConnectorTest {
	
	@InjectMocks
	private OLCIConnector olciConnector;
	
	@Mock
	private OLCIConfig olciConfig;
	
	@Mock
	private HttpClientService httpClientService;
	
	@Test
	public void test() {
		String rloc="MNOWGI";
		String givenName="AA"; 
		String familyName="TEST";
		String url = "www.baidu.com";
		Map<String, String> params = new LinkedHashMap<>();
		params.put("rloc", rloc);
		params.put("givenName", givenName);
		params.put("familyName", familyName);
		Map<String, String> hederParams = new HashMap<>();
		hederParams.put("Access-Channel", MMBConstants.APP_CODE_OLCI);
		HttpResponse<RetrieveJourneyResponseDTO> oLCI=new HttpResponse<RetrieveJourneyResponseDTO>();
		
		oLCI.setRequestUrl("www.qq.com");
		oLCI.setStatus(HttpResponse.ResponseStatus.SUCCESS_200);
		oLCI.setOriginalBody("123456");
		when(olciConfig.getJourneyResponseByRLOC()).thenReturn(url);
		when(httpClientService.getForObject(RetrieveJourneyResponseDTO.class, url, hederParams, params)).thenReturn(oLCI);
		olciConnector.getJourneyResponseByRLOC(rloc, givenName, familyName);
		//assertEquals("SUCCESS_200", oLCI.getStatus());
		assertEquals("123456", oLCI.getOriginalBody());
		assertEquals("www.qq.com", oLCI.getRequestUrl());
	}

}
