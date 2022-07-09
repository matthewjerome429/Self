package com.cathaypacific.mmbbizrule.dto.response.commonapi.destinationinfo;

import com.cathaypacific.mmbbizrule.model.booking.detail.CurrencyInfo;
import com.cathaypacific.mmbbizrule.model.common.destination.PowerInfo;
import com.cathaypacific.mmbbizrule.model.common.destination.WeatherInfoAvg;

public class DestinationInfoDTO {

	private CurrencyInfo currencyInfo;
	
	private PowerInfo powerInfo;
	
	private WeatherInfoAvg weatherInfoAvg;

	public CurrencyInfo getCurrencyInfo() {
		return currencyInfo;
	}

	public void setCurrencyInfo(CurrencyInfo currencyInfo) {
		this.currencyInfo = currencyInfo;
	}

	public PowerInfo getPowerInfo() {
		return powerInfo;
	}

	public void setPowerInfo(PowerInfo powerInfo) {
		this.powerInfo = powerInfo;
	}

	public WeatherInfoAvg getWeatherInfoAvg() {
		return weatherInfoAvg;
	}

	public void setWeatherInfoAvg(WeatherInfoAvg weatherInfoAvg) {
		this.weatherInfoAvg = weatherInfoAvg;
	}

}
