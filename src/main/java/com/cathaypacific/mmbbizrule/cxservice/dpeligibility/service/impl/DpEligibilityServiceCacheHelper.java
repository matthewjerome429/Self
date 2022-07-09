package com.cathaypacific	.mmbbizrule.cxservice.dpeligibility.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.aop.tokenlevelcache.TokenLevelCacheable;
import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.config.DpEligibilityConfig;
import com.cathaypacific.mmbbizrule.cxservice.dpeligibility.model.journey.DpEligibilityJourneyResponse;
import com.cathaypacific.mmbbizrule.util.HttpClientService;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.google.gson.Gson;

@Component
public class DpEligibilityServiceCacheHelper {

	private static final Gson GSON = new Gson();
	
	private static final LogAgent LOGGER = LogAgent.getLogAgent(DpEligibilityServiceCacheHelper.class);
	
	//private static final String PARAM_ONEA_RLOC = "rloc";

	@Autowired
	private DpEligibilityConfig dpEligibilityConfig;

	@Autowired
	private HttpClientService httpClientService;
	
	@TokenLevelCacheable(name=TokenCacheKeyEnum.DP_ELIGIBILITY_JOURNEY)
	@LogPerformance(message = "Time required to call dp eligibility for journey.")
	public DpEligibilityJourneyResponse getJourneys(PNRReply pnr) throws BusinessBaseException {

		DpEligibilityJourneyResponse dpEligibilityJourney = null;
		String response = null;
		
		try {
			response = httpClientService.postJson(
							dpEligibilityConfig.getJourneyUrl(),
							GSON.toJson(pnr)
						);
			if (!StringUtils.isEmpty(response)) {
				dpEligibilityJourney = GSON.fromJson(response, DpEligibilityJourneyResponse.class);
				
				if (dpEligibilityJourney.getError() != null || !dpEligibilityJourney.isSuccess()) {
					LOGGER.warn("Failed to get journey. Error found or 'success' flag is false in DPEligibility journey response.");
					return null;
				}
			}
		} catch (Exception e) {
			throw new ExpectedException("Cannot connect dp eligibility service." + e.getMessage(),
					new ErrorInfo(ErrorCodeEnum.ERR_AEP_CONNECTION), e);
		}

		return dpEligibilityJourney;
	}
	// Retrieve PNRReply logic, replacing call DpEligibility with call OneA
	/*@LogPerformance(message = "Time required to call dp eligibility for journey.")
	public DpEligibilityPNRReply getPnr(String oaRloc) throws BusinessBaseException {

		String url = UriComponentsBuilder.fromHttpUrl(dpEligibilityConfig.getPnrUrl())
				.queryParam(PARAM_ONEA_RLOC, oaRloc)
				.build().toString();

		HttpResponse<DpEligibilityPNRReply> pnr;

		try {
			Map<String, String> header = Maps.newHashMap();
			header.put("Accept", "application/json");
			Map<String, String> paramMappings = Maps.newHashMap();
			pnr = httpClientService.getForObject(DpEligibilityPNRReply.class, url, header, paramMappings);
		} catch (Exception e) {
			throw new ExpectedException("Cannot connect dp eligibility service." + e.getMessage(),
					new ErrorInfo(ErrorCodeEnum.ERR_AEP_CONNECTION), e);
		}

		return pnr.getValue();
	}*/
}
