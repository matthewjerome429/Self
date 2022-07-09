package com.cathaypacific.mmbbizrule.config.cache;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import com.cathaypacific.mbcommon.constants.CacheNamesConstants;


@Component
@PropertySource(value = { "config/cachetime.properties" })
@ConfigurationProperties
public class CacheTimeOutConfig {
	
	private Long defaultExpiration;
	
	private Long defaultExpirationAem;
	
	private Long timeZone;

	private Long flightStatus;

	private Long countrycodeTwoThreeMap;
	
	private Long airFlightInfo;
	
	private Long ticketProcess;

	private Long aepBaggage;
	
	private Long countryCode;
	
	private Long cityAirPorts;
	
	private Long airportCity;
	
	private Long umnrEformPdfTemplate;
	
	private Long aemAirportCodeByName;
	
	private Long aemCountrySelector;
	
	private Long tbOpenCloseTime;
	
	private Long tbTravelDocDisplay;
	
	private Long tbTravelDocList;
	
	private Long tbTravelDocOd;

	private Long openCloseTime;

	private Long travelDocDisplay;

	private Long travelDocList;

	private Long travelDocOd;

	private Long travelDocNatCoiCheck;

	public Map<String, Long> getCacheTimeoutExpiresMap(){
		Map<String, Long> expires = new HashMap<>();
		
		//the default value of these property is defaultExpiration
		expires.put(CacheNamesConstants.TIMEZONE, getTimeZone());
		expires.put(CacheNamesConstants.FLIGHTSTATUS, getFlightStatus());
		expires.put(CacheNamesConstants.COUNTRYCODE_TWO_THREE_MAP, getCountrycodeTwoThreeMap());
		expires.put(CacheNamesConstants.AIR_FLIGHT_INFO, getAirFlightInfo());
		expires.put(CacheNamesConstants.AEP_BAGGAGE, getAepBaggage());
		
		//the default value of these property is defaultExpirationAem
		expires.put(CacheNamesConstants.COUNTRYCODE, getCountryCode());
		expires.put(CacheNamesConstants.CITYAIRPORTS, getCityAirPorts());
		expires.put(CacheNamesConstants.AIRPORTCITY, getAirportCity());
		expires.put(CacheNamesConstants.UMNR_EFORM_PDF_TEMPLATE, getUmnrEformPdfTemplate());
		expires.put(CacheNamesConstants.AIRPORTCODEBYNAME, getAemAirportCodeByName());
		expires.put(CacheNamesConstants.COUNTRY_SELECTOR, getAemCountrySelector());
		
		// expire time for database tables
		expires.put(CacheNamesConstants.TB_OPEN_CLOSE_TIME, getTbOpenCloseTime());
		expires.put(CacheNamesConstants.TB_TRAVEL_DOC_DISPLAY, getTbTravelDocDisplay());
		expires.put(CacheNamesConstants.TB_TRAVEL_DOC_LIST, getTbTravelDocList());
		expires.put(CacheNamesConstants.TB_TRAVEL_DOC_OD, getTbTravelDocOd());

		//expire time for olci call for db
		expires.put(CacheNamesConstants.OPEN_CLOSE_TIME, getOpenCloseTime());
		expires.put(CacheNamesConstants.TRAVEL_DOC_DISPLAY, getTravelDocDisplay());
		expires.put(CacheNamesConstants.TRAVEL_DOC_LIST, getTravelDocList());
		expires.put(CacheNamesConstants.TRAVEL_DOC_OD, getTravelDocOd());
		expires.put(CacheNamesConstants.TRAVEL_DOC_NAT_COI_CHECK, getTravelDocNatCoiCheck());

		return expires;
	}

	public Long getDefaultExpiration() {
		return defaultExpiration;
	}
	
	public Long getDefaultExpirationAem() {
		return defaultExpirationAem;
	}

	public Long getTimeZone() {
		return timeZone == 0 ? defaultExpiration: timeZone;
	}

	public Long getFlightStatus() {
		return flightStatus == 0 ? defaultExpiration: flightStatus;
	}
	
	public Long getAepBaggage() {
		return aepBaggage == 0 ? defaultExpiration:aepBaggage;
	}

	public Long getAirFlightInfo() {
		return airFlightInfo == 0 ? defaultExpiration: airFlightInfo;
	}
	
	public Long getCountrycodeTwoThreeMap() {
		return countrycodeTwoThreeMap == 0 ? defaultExpiration: countrycodeTwoThreeMap;
	}
	
	public Long getCountryCode() {
		return countryCode == 0 ? defaultExpirationAem: countryCode;
	}
	
	public Long getCityAirPorts() {
		return cityAirPorts ==0 ? defaultExpirationAem: cityAirPorts;
	}
	
	public Long getAirportCity() {
		return airportCity == 0 ? defaultExpirationAem: airportCity;
	}
	
	public Long getAemAirportCodeByName() {
		 return airportCity == 0 ? defaultExpirationAem: aemAirportCodeByName;
	}
	
	public Long getUmnrEformPdfTemplate() {
		return umnrEformPdfTemplate == 0 ? defaultExpirationAem : umnrEformPdfTemplate;
	}
	
	public Long getAemCountrySelector() {
		return aemCountrySelector == 0 ? defaultExpirationAem : aemCountrySelector;
	}
	
	public Long getTbOpenCloseTime() {
		return tbOpenCloseTime == 0 ? defaultExpirationAem : tbOpenCloseTime;
	}

	public Long getTbTravelDocDisplay() {
		return tbTravelDocDisplay == 0 ? defaultExpirationAem : tbTravelDocDisplay;
	}

	public Long getTbTravelDocList() {
		return tbTravelDocList == 0 ? defaultExpirationAem : tbTravelDocList;
	}

	public Long getTbTravelDocOd() {
		return tbTravelDocOd == 0 ? defaultExpirationAem : tbTravelDocOd;
	}

	public void setDefaultExpirationAem(Long defaultExpirationAem) {
		this.defaultExpirationAem = defaultExpirationAem;
	}

	public void setTimeZone(Long timeZone) {
		this.timeZone = timeZone;
	}

	public void setFlightStatus(Long flightStatus) {
		this.flightStatus = flightStatus;
	}

	public void setAirFlightInfo(Long airFlightInfo) {
		this.airFlightInfo = airFlightInfo;
	}

	public void setTicketProcess(Long ticketProcess) {
		this.ticketProcess = ticketProcess;
	}

	public void setCountrycodeTwoThreeMap(Long countrycodeTwoThreeMap) {
		this.countrycodeTwoThreeMap = countrycodeTwoThreeMap;
	}

	public void setCountryCode(Long countryCode) {
		this.countryCode = countryCode;
	}

	public void setCityAirPorts(Long cityAirPorts) {
		this.cityAirPorts = cityAirPorts;
	}

	public void setAepBaggage(Long aepBaggage) {
		this.aepBaggage = aepBaggage;
	}
	
	public void setAirportCity(Long airportCity) {
		this.airportCity = airportCity;
	}

	public void setUmnrEformPdfTemplate(Long umnrEformPdfTemplate) {
		this.umnrEformPdfTemplate = umnrEformPdfTemplate;
	}

	public void setAemAirportCodeByName(Long aemAirportCodeByNameCache) {
		this.aemAirportCodeByName = aemAirportCodeByNameCache;
	}

	public void setAemCountrySelector(Long aemCountrySelector) {
		this.aemCountrySelector = aemCountrySelector;
	}
	
	public void setTbOpenCloseTime(Long tbOpenCloseTimeLong) {
		this.tbOpenCloseTime = tbOpenCloseTimeLong;
	}

	public void setTbTravelDocDisplay(Long tbTravelDocDisplay) {
		this.tbTravelDocDisplay = tbTravelDocDisplay;
	}

	public void setTbTravelDocList(Long tbTravelDocList) {
		this.tbTravelDocList = tbTravelDocList;
	}

	public void setTbTravelDocOd(Long tbTravelDocOd) {
		this.tbTravelDocOd = tbTravelDocOd;
	}

	public void setDefaultExpiration(Long defaultExpiration) {
		this.defaultExpiration = defaultExpiration;
	}

	public Long getOpenCloseTime() {
		return openCloseTime == 0 ? defaultExpirationAem : openCloseTime;
	}

	public void setOpenCloseTime(Long openCloseTime) {
		this.openCloseTime = openCloseTime;
	}

	public Long getTravelDocDisplay() {
		return travelDocDisplay == 0 ? defaultExpirationAem : travelDocDisplay;
	}

	public void setTravelDocDisplay(Long travelDocDisplay) {
		this.travelDocDisplay = travelDocDisplay;
	}

	public Long getTravelDocList() {
		return travelDocList == 0 ? defaultExpirationAem : travelDocList;
	}

	public void setTravelDocList(Long travelDocList) {
		this.travelDocList = travelDocList;
	}

	public Long getTravelDocOd() {
		return travelDocOd == 0 ? defaultExpirationAem : travelDocOd;
	}

	public void setTravelDocOd(Long travelDocOd) {
		this.travelDocOd = travelDocOd;
	}

	public Long getTravelDocNatCoiCheck() {
		return travelDocNatCoiCheck == 0 ? defaultExpirationAem : travelDocNatCoiCheck;
	}

	public void setTravelDocNatCoiCheck(Long travelDocNatCoiCheck) {
		this.travelDocNatCoiCheck = travelDocNatCoiCheck;
	}
}
