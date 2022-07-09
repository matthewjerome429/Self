package com.cathaypacific.mmbbizrule.business.commonapi;

import com.cathaypacific.mmbbizrule.model.booking.detail.CurrencyInfo;
import com.cathaypacific.mmbbizrule.model.common.destination.PowerInfo;
import com.cathaypacific.mmbbizrule.model.common.destination.WeatherInfoAvg;

public interface DestinationInfoBusiness {

	/**
	 * get power info by country code
	 * @param countryCode
	 * @return
	 */
	public PowerInfo getPowerInfoByCountryCode(String countryCode);
	
	/**
	 * get currency info by country code
	 * @param countryCode
	 * @return
	 */
	public CurrencyInfo getCurrencyInfoByCountryCode(String countryCode);
	
	/**
	 * get average weather info by port code
	 * @param portCode
	 * @param arriveDate
	 * @return
	 */
	public WeatherInfoAvg getWeatherInfoByPortCode(String portCode, String arriveDate);
}
