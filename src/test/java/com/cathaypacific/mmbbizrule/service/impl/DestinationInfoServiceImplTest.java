package com.cathaypacific.mmbbizrule.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.db.dao.CountryCurrencyDAO;
import com.cathaypacific.mmbbizrule.db.dao.PowerSocketTypeDAO;
import com.cathaypacific.mmbbizrule.db.dao.PowerVoltageFrequencyDAO;
import com.cathaypacific.mmbbizrule.db.dao.WeatherHistoricalAvgDAO;
import com.cathaypacific.mmbbizrule.db.model.CountryCurrencyMap;
import com.cathaypacific.mmbbizrule.db.model.PowerSocketType;
import com.cathaypacific.mmbbizrule.db.model.PowerVoltageFrequency;
import com.cathaypacific.mmbbizrule.model.booking.detail.CurrencyInfo;

@RunWith(MockitoJUnitRunner.class)
public class DestinationInfoServiceImplTest {

	@InjectMocks
	private DestinationInfoServiceImpl destinationInfoServiceImpl;
	
	@Mock
	private PowerSocketTypeDAO powerSocketTypeDAO;
	
	@Mock
	private PowerVoltageFrequencyDAO powerVoltageDAO;
	
	@Mock
	private CountryCurrencyDAO countryCurrencyDAO;
	
	@Mock
	private WeatherHistoricalAvgDAO weatherHistoricalAvgDAO;
	
	@Test
	public void test_getPowerVoltageByCountryCode() {
		PowerVoltageFrequency powerVoltage = new PowerVoltageFrequency();
		powerVoltage.setPowerVoltage("120V");
		
		when(powerVoltageDAO.findByAppCodeAndCountryCode(MMBConstants.APP_CODE, "ZA")).thenReturn(powerVoltage);
		PowerVoltageFrequency powerVoltageFrequency = destinationInfoServiceImpl.getPowerVoltageFrequencyByCountryCode("ZA");
		assertEquals("120V", powerVoltageFrequency.getPowerVoltage());
	}
	
	@Test
	public void test_getPowerSocketTypeByCountryCode() {
		List<PowerSocketType> powerSocketTypes = new ArrayList<>();
		PowerSocketType powerSocketType = new PowerSocketType();
		powerSocketType.setSocketType("C");
		powerSocketTypes.add(powerSocketType);
		
		when(powerSocketTypeDAO.findByAppCodeAndCountryCode(MMBConstants.APP_CODE, "ZA")).thenReturn(powerSocketTypes);
		List<String> powerSocketList = destinationInfoServiceImpl.getPowerSocketTypeByCountryCode("ZA");
		assertEquals("C", powerSocketList.get(0));
	}
	
	@Test
	public void test_getCountryCurrencyByCountryCode() {
		CountryCurrencyMap currencyInfo = new CountryCurrencyMap();
		currencyInfo.setCountryCode("AF");
		currencyInfo.setCurrencyCode("AFN");
		currencyInfo.setCurrencyName("Afghan Afghani");
		
		when(countryCurrencyDAO.findByAppCodeAndCountryCode(MMBConstants.APP_CODE, "AF")).thenReturn(currencyInfo);
		CurrencyInfo currencyInfoResult = destinationInfoServiceImpl.getCountryCurrencyByCountryCode("AF");
		assertEquals("AFN", currencyInfoResult.getCurrencyCode());
	}
}
