package com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.config.ReferenceDataNationalityConfig;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.model.NationalityCodeResponse;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.NationalityCodeCacheService;
import com.google.gson.Gson;

@Service
public class NationalityCodeCacheServiceImpl implements NationalityCodeCacheService{

	private static LogAgent logger = LogAgent.getLogAgent(NationalityCodeCacheServiceImpl.class);
	@Autowired
	private ReferenceDataNationalityConfig referenceDataNationalityConfig;

	@Autowired
	private RestTemplate restTemplate;

	private static final Gson GSON = new Gson();

	/**
	 * getNationality
	 * 
	 * @return
	 * @throws BusinessBaseException 
	 * @throws BusinessException
	 */
	//@Cacheable(cacheNames = CacheNamesConstants.COUNTRYCODE_TWO_THREE_MAP,keyGenerator="shareKeyGenerator")
	public String getNationality(){
		String nationalityInfo = invokeNationality();
		NationalityCodeResponse nationalityCodeResponse = GSON.fromJson(nationalityInfo, NationalityCodeResponse.class);
		if (nationalityCodeResponse == null || CollectionUtils.isEmpty(nationalityCodeResponse.getNationalityCode())) {
			logger.error("Cannot paser nationality info, response: %s", nationalityInfo);
			//throw exception
		}
		return nationalityInfo;
	}

	//@LogPerformance(message = "Get Nationality Code Mapping.")
	private String invokeNationality(){
		String url = referenceDataNationalityConfig.getReferenceDataNationalityUrl();
		logger.debug("start get nationality call .");
		String result = null;
		try {
			 result = restTemplate.getForObject(url, String.class);
			
		} catch (RuntimeException e) {
			logger.error("Get nationality info failed.");
			throw e;
		}
		if (StringUtils.isEmpty(result)) {
			logger.error("Cannot receive nationality info, the response is empty !");
			//throw new UnexpectedException("Cannot receive nationality info, the response is empty !", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		return result;
	}

}
