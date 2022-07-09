package com.cathaypacific.mmbbizrule.business.commonapi.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mmbbizrule.business.commonapi.DestinationInfoBusiness;
import com.cathaypacific.mmbbizrule.db.model.PowerVoltageFrequency;
import com.cathaypacific.mmbbizrule.model.booking.detail.CurrencyInfo;
import com.cathaypacific.mmbbizrule.model.common.destination.PowerInfo;
import com.cathaypacific.mmbbizrule.model.common.destination.WeatherInfoAvg;
import com.cathaypacific.mmbbizrule.service.DestinationInfoService;

@Service
public class DestinationInfoBusinessImpl implements DestinationInfoBusiness {

	@Autowired
	private DestinationInfoService destinationInfoService;
	
	@Override
	public PowerInfo getPowerInfoByCountryCode(String countryCode) {
		PowerInfo powerInfo = new PowerInfo();
		
		PowerVoltageFrequency powerAndFrequency = destinationInfoService.getPowerVoltageFrequencyByCountryCode(countryCode);
		
		if(powerAndFrequency != null) {
			powerInfo.setPowerFrequency(powerAndFrequency.getPowerFrequency());
			powerInfo.setPowerVoltage(powerAndFrequency.getPowerVoltage());
		} else {
			powerInfo.setPowerFrequency("");
			powerInfo.setPowerVoltage("");
		}		
		powerInfo.setCountryCode(countryCode);
		powerInfo.setSocketType(destinationInfoService.getPowerSocketTypeByCountryCode(countryCode));
		return powerInfo;
	}

	@Override
	public CurrencyInfo getCurrencyInfoByCountryCode(String countryCode) {

		return destinationInfoService.getCountryCurrencyByCountryCode(countryCode);
	}

	@Override
	public WeatherInfoAvg getWeatherInfoByPortCode(String portCode, String arriveDate) {
		
		return destinationInfoService.getWeatherInfoAvgByPortCode(portCode, arriveDate);
	}
	
	

}
