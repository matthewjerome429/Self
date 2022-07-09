package com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.Impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mmbbizrule.config.ReferenceDataTimeZoneConfig;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.model.TimeZone;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.model.TimeZoneInfoForMapResponse;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneServiceCacheHelper;
import com.cathaypacific.mmbbizrule.util.HttpClientService;

@Service
public class AirportTimeZoneServiceCacheHelperImpl implements AirportTimeZoneServiceCacheHelper {
	
	private static LogAgent logger = LogAgent.getLogAgent(AirportTimeZoneServiceCacheHelperImpl.class);
	
	@Autowired
	private HttpClientService httpClientService;
	
	@Autowired
	private ReferenceDataTimeZoneConfig referenceDataConfig;

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.TIMEZONE, keyGenerator = "shareKeyGenerator")
	@LogPerformance(message = "Get timeZone Mapping.")
	public Map<String, String> getAllAirPortTimeZoneOffset() {
		logger.debug("Start get TimeZone call.");
		Map<String, String> result = null;

		TimeZoneInfoForMapResponse timeZoneInfoForMapResponse = httpClientService
				.get(referenceDataConfig.getReferenceDataTimeZoneUrl(), TimeZoneInfoForMapResponse.class);
		result = buildTimeZoneMap(timeZoneInfoForMapResponse);

		return result;
	}
	
	private Map<String, String> buildTimeZoneMap(TimeZoneInfoForMapResponse timeZoneInfoForMapResponse) {
		Map<String, String> result = null;
		if (timeZoneInfoForMapResponse != null && timeZoneInfoForMapResponse.getTimeZone() != null
				&& !timeZoneInfoForMapResponse.getTimeZone().isEmpty()) {
			result = new HashMap<>();
			for (TimeZone timeZone : timeZoneInfoForMapResponse.getTimeZone()) {
				result.put(timeZone.getAirportCode(), timeZone.getOffset());
			}
		}
		return result;
	}

}
