package com.cathaypacific.mmbbizrule.service;

import java.util.List;

import com.cathaypacific.mmbbizrule.db.model.PowerVoltageFrequency;
import com.cathaypacific.mmbbizrule.model.booking.detail.CurrencyInfo;
import com.cathaypacific.mmbbizrule.model.common.destination.WeatherInfoAvg;

public interface DestinationInfoService {
	
	/**
	 * get power socket type list by country code
	 * @param countryCode
	 * @return
	 */
	public List<String> getPowerSocketTypeByCountryCode(String countryCode);
	
	/**
	 * get power voltage and frequency by country code
	 * @param countryCode
	 * @return
	 */
	public PowerVoltageFrequency getPowerVoltageFrequencyByCountryCode(String countryCode);
	
	/**
	 * get country currency info by country code
	 * @param countryCode
	 * @return
	 */
	public CurrencyInfo getCountryCurrencyByCountryCode(String countryCode);
	
	/**
	 * get average weather info by port code and month
	 * @param portCode
	 * @param arriveDate
	 * @return
	 */
	public WeatherInfoAvg getWeatherInfoAvgByPortCode(String portCode, String arriveDate);

}
