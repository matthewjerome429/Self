package com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.impl;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mmbbizrule.config.ReferenceDataNationalityConfig;

import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;

@RunWith(MockitoJUnitRunner.class)
public class NationalityCodeCacheServiceImplTest {
	
	@InjectMocks
	NationalityCodeCacheServiceImpl nationalityCodeCacheServiceImpl;
	
	@Mock
	ConstantDataDAO constantDataDAO;
	
	@Mock
	private ReferenceDataNationalityConfig referenceDataNationalityConfig;

	@Mock
	private RestTemplate restTemplate;
	
	@Test
	public void getNationality_test() {
		String url="www.qq.com";
		@SuppressWarnings("unchecked")
		ResponseEntity<String> result=Mockito.mock(ResponseEntity.class);
		when(result.getStatusCode()).thenReturn(HttpStatus.OK);
		when(referenceDataNationalityConfig.getReferenceDataNationalityUrl()).thenReturn(url);
		when(restTemplate.getForEntity(url, String.class)).thenReturn(result);
		String Info=nationalityCodeCacheServiceImpl.getNationality();
		Assert.assertEquals(null, Info);
	}

}
