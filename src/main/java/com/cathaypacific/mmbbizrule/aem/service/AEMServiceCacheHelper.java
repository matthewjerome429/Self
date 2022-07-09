package com.cathaypacific.mmbbizrule.aem.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mmbbizrule.aem.model.AllAirportsInfo;
import com.cathaypacific.mmbbizrule.aem.model.SelectorOptions;

@Service
public class AEMServiceCacheHelper {

	@Value("${aem.endpoint.airportInfo}")
	private String aemAirPortInfoUrl;
	
	@Value("${aem.endpoint.allairportInfo}")
	private String aemAllAirPortInfoUrl;
	
	@Value("${aem.endpoint.country}")
	private String aemCountrySelectorUrl;
	
	@Value("${aem.endpoint.umnrEFormPDFTemplate}")
	private String umnrEFormPDFTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	private static final LogAgent LOGGER = LogAgent.getLogAgent(AEMServiceCacheHelper.class);
	
	private static final String AEM_ALL_AIRPORT_INFO_URL_PLACEHOLDER_LANGUAGE = "%LANGUAGE%";

	/**
	 * Get country code by airport code, AEM web-service is called to retrieve
	 * Airport details
	 */
	@LogPerformance(message = "Time required to get all air ports info.")
	@Cacheable(cacheNames = CacheNamesConstants.COUNTRYCODE, keyGenerator = "shareKeyGenerator")
	public AllAirportsInfo allAirportsInfo(Locale locale) {
		try {
			if(!StringUtils.isEmpty(aemAllAirPortInfoUrl)) {
				aemAllAirPortInfoUrl = aemAllAirPortInfoUrl.replaceAll(AEM_ALL_AIRPORT_INFO_URL_PLACEHOLDER_LANGUAGE, locale.getLanguage());
			}
			AllAirportsInfo allAirportsInfo = restTemplate.getForObject(aemAllAirPortInfoUrl, AllAirportsInfo.class);

			if (!CollectionUtils.isEmpty(allAirportsInfo.getAirports())
					&& CollectionUtils.isEmpty(allAirportsInfo.getErrors())) {
				return allAirportsInfo;
			} else {
				LOGGER.warn("Retrieve air ports info faild.");
			}

			return null;
		} catch (Exception e) {
			LOGGER.error("Retrieve air ports failed.", e);
			return null;
		}

	}
	
	/**
	 * Country list
	 */
	@LogPerformance(message = "Time required to get all air ports info.")
	@Cacheable(cacheNames = CacheNamesConstants.COUNTRY_SELECTOR, keyGenerator = "shareKeyGenerator")
	public SelectorOptions getCountrySelectors(Locale locale) {
		try {
			if(!StringUtils.isEmpty(aemCountrySelectorUrl)) {
				aemCountrySelectorUrl = aemCountrySelectorUrl.replaceAll(AEM_ALL_AIRPORT_INFO_URL_PLACEHOLDER_LANGUAGE, locale.getLanguage());
			}
			SelectorOptions selectorOptions = restTemplate.getForObject(aemCountrySelectorUrl, SelectorOptions.class);

			if (!CollectionUtils.isEmpty(selectorOptions.getSelectorOption())) {
				return selectorOptions;
			} else {
				LOGGER.warn("Retrieve country selector option faild.");
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("Retrieve country selector option faild.", e);
			return null;
		}

	}

	/**
	 * Retrieve UMNR EForm PDF template from redis / AEM
	 * @return a base64 encoded string from bytes. Decode it by base64 to bytes before using it.
	 * @throws IOException
	 */
	@Cacheable(cacheNames = CacheNamesConstants.UMNR_EFORM_PDF_TEMPLATE)
	@LogPerformance(message = "Time required to get UMNR EForm PDF template.")
	public String getUMNREFormPDFTemplate() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<Resource> responseEntity = restTemplate.exchange(umnrEFormPDFTemplateUrl, HttpMethod.GET, entity, Resource.class);
		
		BufferedInputStream inputStream = new BufferedInputStream(responseEntity.getBody().getInputStream());
		String templateBytesStr = Base64.getEncoder().encodeToString(StreamUtils.copyToByteArray(inputStream));
		inputStream.close();
		
		return templateBytesStr;
	}
}
