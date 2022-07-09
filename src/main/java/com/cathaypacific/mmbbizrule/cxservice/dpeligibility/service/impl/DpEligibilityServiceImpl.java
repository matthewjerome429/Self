package com.cathaypacific.mmbbizrule.cxservice.dpeligibility.service.impl;

import java.net.URI;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.config.DpEligibilityConfig;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.atcdw.ChangeFlightEligibleResponse;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.journey.DpEligibilityJourneyResponse;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.service.DpEligibilityService;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

@Service
public class DpEligibilityServiceImpl implements DpEligibilityService {

	@Autowired
	private DpEligibilityServiceCacheHelper dpEligibilityServiceCacheHelper;
	
	@Autowired
	private DpEligibilityConfig dpEligibilityConfig;
	
	@Autowired
	private RestTemplate restTemplate;
	@Async
	@Override
	//@LogPerformance(message = "Time required to start async method.")
	public Future<DpEligibilityJourneyResponse> asyncGetJourney(PNRReply pnr) throws BusinessBaseException {
		return new AsyncResult<>(getJourney(pnr));
	}
	
	@Override
	//@LogPerformance(message = "Time required to start async method.")
	public DpEligibilityJourneyResponse getJourney(PNRReply pnr) throws BusinessBaseException {
		if(pnr == null) {
			return null;
		}	
		return dpEligibilityServiceCacheHelper.getJourneys(pnr);
	}

	@Override
	public ChangeFlightEligibleResponse getAtcDwInfo(PNRReply pnr, String officeId) throws BusinessBaseException {
		if (pnr == null) {
			return null;
		}
		URI uri = UriComponentsBuilder.fromHttpUrl(dpEligibilityConfig.getAtcDwPnrUrl()).queryParam("appCode",MMBConstants.APP_CODE).queryParam("flow", "ATC").queryParam("pseudoCityCode", officeId).build().toUri();
		ResponseEntity<ChangeFlightEligibleResponse> atcResponse =
				restTemplate.postForEntity(uri, pnr, ChangeFlightEligibleResponse.class);
		return atcResponse.getBody();
	}
	
}
