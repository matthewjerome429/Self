package com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.impl;


import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NationalityCodeServiceImplTest {
	
	@InjectMocks
	NationalityCodeServiceImpl nationalityCodeServiceImpl;
	
	@Mock
	NationalityCodeCacheServiceImpl nationalityCodeCacheService;
	
	@Test
	public void test() {
		String countryCode="USA";
		String codeInfo="{\"nationalityCode\":[{\"alpha2Code\":\"USA\",\"alpha3Code\":\"HK\"},{}]}";
		when(nationalityCodeCacheService.getNationality()).thenReturn(codeInfo);
		String result=nationalityCodeServiceImpl.findThreeCountryCodeByTwoCountryCode(countryCode);
		Assert.assertEquals("HK", result);
	}
	
	@Test
	public void findTwoCountryCodeByThreeCountryCode_test() {
		String countryCode="HK";
		String codeInfo="{\"nationalityCode\":[{\"alpha2Code\":\"USA\",\"alpha3Code\":\"HK\"},{}]}";
		when(nationalityCodeCacheService.getNationality()).thenReturn(codeInfo);
		String result=nationalityCodeServiceImpl.findTwoCountryCodeByThreeCountryCode(countryCode);
		Assert.assertEquals("USA", result);
	}
	
}
