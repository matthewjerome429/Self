package com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneService;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneServiceCacheHelper;


@Service
public class AirportTimeZoneServiceImpl implements AirportTimeZoneService{
	
	private static LogAgent logger = LogAgent.getLogAgent(AirportTimeZoneServiceImpl.class);
	
//	Marked as Waiting for OLCI TimeZone service feng.d.li
/*	@Autowired
	private HttpClientService httpClientService;
	
	@Autowired
	private OLCIConfig oLCIConfig;
	
	@Cacheable(cacheNames=CacheNamesConstants.TIMEZONE, keyGenerator = "shareKeyGenerator")
	@LogPerformance(message = "Get timeZone Mapping.")
	public String getAirPortTimeZoneOffset(String portCode) {

		logger.debug("Start get TimeZone call.");
		String result = null;
		TimeZoneInformationResponse timeZoneInformationResponse = httpClientService
				.get(oLCIConfig.getTimezoneURL().replace("{airport}", portCode).replace("{time}", new SimpleDateFormat(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM).format(new Date())), TimeZoneInformationResponse.class);
		result = buildTimeZone(timeZoneInformationResponse);
		return result;
	}
	
	private String buildTimeZone(TimeZoneInformationResponse timeZoneInformationResponse) {
		String result = null;
		if (timeZoneInformationResponse != null && timeZoneInformationResponse.getErrors().isEmpty()) {
			result = timeZoneInformationResponse.getLocaleTimeOffset();
		}
		return result;
	}*/
	@Autowired
	private AirportTimeZoneServiceCacheHelper airportTimeZoneServiceCacheHelper;
	
	@Cacheable(cacheNames=CacheNamesConstants.TIMEZONE, keyGenerator = "shareKeyGenerator")
	//@LogPerformance(message = "Get timeZone Mapping.")
	public String getAirPortTimeZoneOffset(String portCode) {
		logger.debug("Start get TimeZone call.");
		return airportTimeZoneServiceCacheHelper.getAllAirPortTimeZoneOffset().get(portCode);
	}
	
	
}
