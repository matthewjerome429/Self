package com.cathaypacific.mmbbizrule.service.impl;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.business.impl.BookingCancelBusinessImpl;
import com.cathaypacific.mmbbizrule.db.dao.CountryCurrencyDAO;
import com.cathaypacific.mmbbizrule.db.dao.PowerSocketTypeDAO;
import com.cathaypacific.mmbbizrule.db.dao.PowerVoltageFrequencyDAO;
import com.cathaypacific.mmbbizrule.db.dao.WeatherHistoricalAvgDAO;
import com.cathaypacific.mmbbizrule.db.model.CountryCurrencyMap;
import com.cathaypacific.mmbbizrule.db.model.PowerSocketType;
import com.cathaypacific.mmbbizrule.db.model.PowerVoltageFrequency;
import com.cathaypacific.mmbbizrule.db.model.WeatherHistoricalAvg;
import com.cathaypacific.mmbbizrule.model.booking.detail.CurrencyInfo;
import com.cathaypacific.mmbbizrule.model.common.destination.WeatherInfoAvg;
import com.cathaypacific.mmbbizrule.service.DestinationInfoService;

@Service
public class DestinationInfoServiceImpl implements DestinationInfoService {
	
	static String[] monthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private static LogAgent logger = LogAgent.getLogAgent(BookingCancelBusinessImpl.class);
	
	@Autowired
	private PowerSocketTypeDAO powerSocketTypeDAO;
	
	@Autowired
	private PowerVoltageFrequencyDAO powerVoltageFrequencyDAO;
	
	@Autowired
	private CountryCurrencyDAO countryCurrencyDAO;
	
	@Autowired
	private WeatherHistoricalAvgDAO weatherHistoricalAvgDAO;

	@Override
	public PowerVoltageFrequency getPowerVoltageFrequencyByCountryCode(String countryCode) {
		return powerVoltageFrequencyDAO.findByAppCodeAndCountryCode(MMBConstants.APP_CODE, countryCode);

	}

	@Override
	public CurrencyInfo getCountryCurrencyByCountryCode(String countryCode) {
		CurrencyInfo currencyInfo = new CurrencyInfo();
		
		CountryCurrencyMap countryCurrency =  countryCurrencyDAO.findByAppCodeAndCountryCode(MMBConstants.APP_CODE, countryCode);
		if (countryCurrency != null) {			
			currencyInfo.setCountryCode(countryCode);
			currencyInfo.setCurrencyCode(countryCurrency.getCurrencyCode());
			currencyInfo.setCurrencyName(countryCurrency.getCurrencyName());
		} else {
			currencyInfo.setCountryCode(countryCode);
			currencyInfo.setCurrencyCode("");
			currencyInfo.setCurrencyName("");
		}
		
		return currencyInfo;
	}

	@Override
	public List<String> getPowerSocketTypeByCountryCode(String countryCode) {
		List<PowerSocketType> powerSocketTypes =  powerSocketTypeDAO.findByAppCodeAndCountryCode(MMBConstants.APP_CODE, countryCode);
		return powerSocketTypes.stream().map(PowerSocketType :: getSocketType).collect(Collectors.toList());
	}

	@Override
	public WeatherInfoAvg getWeatherInfoAvgByPortCode(String portCode, String arriveDate) {

		WeatherHistoricalAvg weatherInfo = weatherHistoricalAvgDAO.findByAppCodeAndPortCode(MMBConstants.APP_CODE, portCode);
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();
		if (weatherInfo != null) {			
			try {	
				DateFormat dateFormat = new SimpleDateFormat("MMM",Locale.ENGLISH);
				Date dateInfo = new SimpleDateFormat(DateUtil.DATE_PATTERN_YYYY_MM_DD).parse(arriveDate);//定义起始日期
				String month = dateFormat.format(dateInfo);
				
				weatherInfoAvg = getMonthWeatherInfo(weatherInfo, month);		
				weatherInfoAvg.setDate(arriveDate);
			} catch (ParseException e) {
				logger.error("Failed to parse arrival in getWeatherInfoAvgByPortCode", e);
			}
		}
		return weatherInfoAvg;
	}
	
	/**
	 * Get weather info by month
	 * @param weatherInfo
	 * @param Month
	 * @return WeatherInfoAvg.class
	 */
	private WeatherInfoAvg getMonthWeatherInfo(WeatherHistoricalAvg weatherInfo, String month) {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();		
		String functionName = "get" + month + "WeatherInfo";
		// Use reflect matching method with different month...
		try {
			Method method = weatherInfo.getClass().getMethod(functionName);
			weatherInfoAvg = (WeatherInfoAvg) method.invoke(weatherInfo);
		} catch (NoSuchMethodException e) {
			logger.error("Failed to find method to get weather info in getMonthWeatherInfo", e);
		} catch (Exception e) {
			logger.error("Failed to get weather info in getMonthWeatherInfo", e);
		}

		return weatherInfoAvg;
	}

}
