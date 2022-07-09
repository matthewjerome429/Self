package com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.model.NationalityCode;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.model.NationalityCodeResponse;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.NationalityCodeService;
import com.google.gson.Gson;

@Service
public class NationalityCodeServiceImpl implements NationalityCodeService{

	private static LogAgent logger = LogAgent.getLogAgent(NationalityCodeServiceImpl.class);
	private static final Gson GSON = new Gson();
	
	@Autowired
	private NationalityCodeCacheServiceImpl nationalityCodeCacheService;

	/**
	 * find Three Country Code By Two Country Code
	 * 
	 * @param countryCode
	 * @return
	 * @throws BusinessException
	 */
	@Cacheable(cacheNames = CacheNamesConstants.COUNTRYCODE_TWO_THREE_MAP, keyGenerator = "shareKeyGenerator")
	public String findThreeCountryCodeByTwoCountryCode(String countryCode){
		String result = null;
		NationalityCodeResponse nationalityCodeResponse = this.getNationality();
		List<NationalityCode> nationalityCodes = nationalityCodeResponse.getNationalityCode();
		if (nationalityCodes != null) {
			NationalityCode nationalityCode = nationalityCodes.stream()
					.filter(n -> n.getAlpha2Code().equals(countryCode)).findFirst().orElse(null);
			if (nationalityCode != null) {
				result = nationalityCode.getAlpha3Code();
			}
		}
		if (StringUtils.isEmpty(result)) {
			logger.info("Cannot find three letters country code for " + countryCode);
		} else {
			logger.info("Found Three Country Code By Two Country Code " + countryCode + ": " + result);
		}
		return result;
	}

	
	@Cacheable(cacheNames = CacheNamesConstants.COUNTRYCODE_TWO_THREE_MAP, keyGenerator = "shareKeyGenerator")
	public String findTwoCountryCodeByThreeCountryCode(String countryCode) {
		String result = null;
		NationalityCodeResponse nationalityCodeResponse = this.getNationality();
		List<NationalityCode> nationalityCodes = nationalityCodeResponse.getNationalityCode();
		if (nationalityCodes != null) {
			NationalityCode nationalityCode = nationalityCodes.stream()
					.filter(n -> n.getAlpha3Code().equals(countryCode)).findFirst().orElse(null);
			if (nationalityCode != null) {
				result = nationalityCode.getAlpha2Code();
			}
		}
		if (StringUtils.isEmpty(result)) {
			logger.info("Cannot find two bits country code for " + countryCode);
		} else {
			logger.info("Found Two Country Code By Three Country Code " + countryCode + ": " + result);
		}
		return result;
	}
	
	/**
	 * get Nationality
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private NationalityCodeResponse getNationality() {
		String codeInfo = nationalityCodeCacheService.getNationality();
		return GSON.fromJson(codeInfo, NationalityCodeResponse.class);
	}
}
