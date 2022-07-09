package com.cathaypacific.mmbbizrule.aem.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.aem.model.AirportDetails;
import com.cathaypacific.mmbbizrule.aem.model.AirportFullInfo;
import com.cathaypacific.mmbbizrule.aem.model.AllAirportsInfo;
import com.cathaypacific.mmbbizrule.aem.model.City;
import com.cathaypacific.mmbbizrule.aem.model.Country;
import com.cathaypacific.mmbbizrule.aem.model.SelectorOption;
import com.cathaypacific.mmbbizrule.aem.model.SelectorOptions;

@Service
public class AEMService {

	@Value("${aem.endpoint.airportInfo}")
	private String aemAirPortInfoUrl;
	
	@Value("${aem.endpoint.allairportInfo}")
	private String aemAllAirPortInfoUrl;

	@Autowired
	private AEMServiceCacheHelper aemServiceCacheHelper;

	private static final LogAgent LOGGER = LogAgent.getLogAgent(AEMService.class);

	/**
	 * Get country code by airport code, AEM web-service is called to retrieve
	 * Airport details
	 */

	//@LogPerformance(message = "Time required to get the country code by airport code")
	@Cacheable(cacheNames = CacheNamesConstants.COUNTRYCODE, keyGenerator = "shareKeyGenerator")
	public String getCountryCodeByPortCode(String portCode) {
		Locale defaultLocale = new Locale("en");
		AllAirportsInfo allAirportsInfo = aemServiceCacheHelper.allAirportsInfo(defaultLocale);
		if (allAirportsInfo != null && !CollectionUtils.isEmpty(allAirportsInfo.getAirports())) {
			
			return allAirportsInfo.getAirports().stream().filter(ap -> portCode.equals(ap.getAirportCode())).findFirst()
					.map(AirportFullInfo::getAirportDetails).map(AirportDetails::getCountry).map(Country::getCode)
					.orElse("");// cache empty if the country code cannot find from the response to avoid frequent invoke to get country code for unknown port
		}
		LOGGER.warn("Cannot find country code for air port:"+portCode);
		return null;
	}
	
	//@LogPerformance(message = "Time required to get the city code by airport code")
	@Cacheable(cacheNames = CacheNamesConstants.CITYCODE, keyGenerator = "shareKeyGenerator")
	public String getCityCodeByPortCode(String portCode) {
		Locale defaultLocale = new Locale("en");
		AllAirportsInfo allAirportsInfo = aemServiceCacheHelper.allAirportsInfo(defaultLocale);
		if (allAirportsInfo != null && !CollectionUtils.isEmpty(allAirportsInfo.getAirports())) {
			
			return allAirportsInfo.getAirports().stream().filter(ap -> portCode.equals(ap.getAirportCode())).findFirst()
					.map(AirportFullInfo::getAirportDetails).map(AirportDetails::getCity).map(City::getCode)
					.orElse(null);
		}
		LOGGER.warn("Cannot find city code for air port:"+portCode);
		return null;
	}
	
	//@LogPerformance(message = "Time required to get the aiports of cities")
	@Cacheable(cacheNames = CacheNamesConstants.CITYAIRPORTS, keyGenerator = "shareKeyGenerator")
	public List<String> airPortListByCityCode (String cityCode){
		Locale defaultLocale = new Locale("en");
		AllAirportsInfo allAirportsInfo = aemServiceCacheHelper.allAirportsInfo(defaultLocale);
		List<String> airPortList = null;
		if (allAirportsInfo != null && !CollectionUtils.isEmpty(allAirportsInfo.getAirports())) {
			airPortList = allAirportsInfo.getAirports().stream().filter(airport->airport.getAirportDetails()!=null
					&&airport.getAirportDetails().getCity()!=null
					&&airport.getAirportDetails().getCity().getCode()!=null
					&& airport.getAirportDetails().getCity().getCode().equals(cityCode)).map(airport->airport.getAirportCode()).collect(Collectors.toList());
		
		}
		 if(CollectionUtils.isEmpty(airPortList)){
			 LOGGER.warn("Cannot find air port list by city code:"+cityCode);
			  airPortList = null;
		 }
		 return airPortList;
		
	}
	
	//@LogPerformance(message = "Time required to get city by airport code")
	@Cacheable(cacheNames = CacheNamesConstants.AIRPORTCITY, keyGenerator = "shareKeyGenerator")
	public City retrieveCityByAirportCode(String airportCode, Locale locale){
		if (locale == null) {
			locale = new Locale("en");
		}
		
		AllAirportsInfo allAirportsInfo = aemServiceCacheHelper.allAirportsInfo(locale);
		Optional<City> city = null;
		if (allAirportsInfo != null && !CollectionUtils.isEmpty(allAirportsInfo.getAirports())) {
			city = allAirportsInfo.getAirports().stream().filter(
					airport->airport.getAirportCode() != null
					&& airport.getAirportCode().equals(airportCode)).map(
							airport->airport.getAirportDetails().getCity()
						).findFirst();
		
		}
		 if(city == null || city.get() == null){
			 LOGGER.warn("Cannot find city by airport code:" + airportCode);
			 return null;
		 }
		 return city.get();
		
	}
	
	//@LogPerformance(message = "Time required to get airport by airport name")
	@Cacheable(cacheNames = CacheNamesConstants.AIRPORTCODEBYNAME, keyGenerator = "shareKeyGenerator")
	public String retrieveAirportCodeByName(String airportName) {
		if(StringUtils.isEmpty(airportName)) {
			return null;
		}
		
		Locale defaultLocale = new Locale("en");
		AllAirportsInfo allAirportsInfo = aemServiceCacheHelper.allAirportsInfo(defaultLocale);
		if(allAirportsInfo == null || CollectionUtils.isEmpty(allAirportsInfo.getAirports())) {
			return null;
		}
		
		AirportFullInfo airportFullInfo = allAirportsInfo.getAirports().stream()
				.filter(airport -> airport.getAirportDetails() != null 
				&& (airportName.equals(airport.getAirportDetails().getAirportFullName()) 
						|| airportName.equals(airport.getAirportDetails().getAirportShortName())
						|| airportName.equals(airport.getAirportDetails().getDefaultAirportFullName())
						|| airportName.equals(airport.getAirportDetails().getDefaultAirportShortName()))
				).findFirst().orElse(null);
		
		if(airportFullInfo == null || airportFullInfo.getAirportCode() == null){
			LOGGER.warn("Cannot find airportCode by airport name:" + airportName);
			return null;
		}
		return airportFullInfo.getAirportCode();
	}
	
	//@LogPerformance(message = "Time required to get airport details by airport code")
	@Cacheable(cacheNames = CacheNamesConstants.AIRPORTDETAIL, keyGenerator = "shareKeyGenerator")
	public AirportDetails getAirportDetailsByCode(String airportCode) {
		if(StringUtils.isEmpty(airportCode)) {
			return null;
		}
		
		Locale defaultLocale = new Locale("en");
		AllAirportsInfo allAirportsInfo = aemServiceCacheHelper.allAirportsInfo(defaultLocale);
		if(allAirportsInfo == null || CollectionUtils.isEmpty(allAirportsInfo.getAirports())) {
			return null;
		}
	
		return allAirportsInfo.getAirports().stream().filter(a -> airportCode.equals(a.getAirportCode()))
				.findFirst().orElse(new AirportFullInfo()).getAirportDetails();
	}
	
	//@LogPerformance(message = "Time required to get the default country name by country code")
	/**
	 * @param countryCode 3-letter country code
	 * @return
	 */
	@Cacheable(cacheNames = CacheNamesConstants.COUNTRYNAME, keyGenerator = "shareKeyGenerator")
	public String getCountryNameByCountryCode(String countryCode) {
		if (StringUtils.isEmpty(countryCode)) {
			return null;
		}
		String result = null;
		Locale defaultLocale = new Locale("en");
		SelectorOptions selectorOptions = aemServiceCacheHelper.getCountrySelectors(defaultLocale);
		if (selectorOptions != null && !CollectionUtils.isEmpty(selectorOptions.getSelectorOption())) {
			result = selectorOptions.getSelectorOption().stream().filter(
						option -> countryCode.equals(option.getKey())
				).findFirst()
				.map(SelectorOption::getDescription)
				.orElse(null);
		}
		
		if (StringUtils.isEmpty(result)) {
			LOGGER.warn("Cannot find country name for country code: " + countryCode);
		}
		return result;
	}
	
	/** 
	 * Retrieve UMNR EForm PDF Template from cache or AEM
	 * @return InputStream
	 * @throws IOException
	 */
	public InputStream getUMNREFormPDFTemplate() throws IOException {
		// We cache base64 encoded string to redis so decode it into buffered input stream before using.
		return new BufferedInputStream(
			new ByteArrayInputStream(
					Base64.getDecoder().decode(aemServiceCacheHelper.getUMNREFormPDFTemplate())
			)
		);
	}
}
