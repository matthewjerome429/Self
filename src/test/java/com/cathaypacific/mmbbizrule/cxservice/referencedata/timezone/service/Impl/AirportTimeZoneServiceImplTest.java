package com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.Impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneServiceCacheHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mmbbizrule.config.ReferenceDataTimeZoneConfig;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.model.TimeZone;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.model.TimeZoneInfoForMapResponse;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneServiceCacheHelper;
import com.cathaypacific.mmbbizrule.util.HttpClientService;

@RunWith(MockitoJUnitRunner.class)
public class AirportTimeZoneServiceImplTest {
	
	@InjectMocks
	AirportTimeZoneServiceImpl airportTimeZoneServiceImpl;
	
	@Mock
	AirportTimeZoneServiceCacheHelper airportTimeZoneServiceCacheHelper = new AirportTimeZoneServiceCacheHelperImpl();
	
	@Mock
	private HttpClientService httpClientService;
	
	@Mock
	private ReferenceDataTimeZoneConfig referenceDataConfig;

	
	@Test
	public void test() {
		TimeZoneInfoForMapResponse timeZoneInfoForMapResponse =new TimeZoneInfoForMapResponse();
		String url="www.baidu.com";
		List<TimeZone> list = new ArrayList<TimeZone>();
		TimeZone tz = new TimeZone();
		tz.setAirportCode("CX");
		tz.setOffset("+0800");
		list.add(tz);
		timeZoneInfoForMapResponse.setTimeZone(list);
		Map<String, String> map = new HashMap<String, String>();
		map.put("CX", "+0800");
		when(referenceDataConfig.getReferenceDataTimeZoneUrl()).thenReturn(url);
		when(httpClientService.get(url, TimeZoneInfoForMapResponse.class)).thenReturn(timeZoneInfoForMapResponse);
		when(airportTimeZoneServiceCacheHelper.getAllAirPortTimeZoneOffset()).thenReturn(map);
		String airPortTimeZoneOffset=airportTimeZoneServiceImpl.getAirPortTimeZoneOffset("CX");
		 Assert.assertEquals("+0800", airPortTimeZoneOffset);
	}

}
