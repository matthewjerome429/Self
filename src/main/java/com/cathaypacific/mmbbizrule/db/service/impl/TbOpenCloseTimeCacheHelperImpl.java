package com.cathaypacific.mmbbizrule.db.service.impl;

import java.util.Date;

import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.olciconsumer.model.response.db.OpenCloseTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mmbbizrule.db.service.TbOpenCloseTimeCacheHelper;

@Service
public class TbOpenCloseTimeCacheHelperImpl implements TbOpenCloseTimeCacheHelper {

	@Autowired
	private OLCIServiceV2 olciServiceV2;

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.OPEN_CLOSE_TIME, keyGenerator = "shareKeyGenerator")
	public OpenCloseTime findByOriginAndAirlineCodeAndAppCodeAndPaxType(
		String origin, String airlineCode, String appCode, String paxType, Date departDate
	) {
		return olciServiceV2.retrieveOpenCloseTimeFromDB(origin,airlineCode,appCode,paxType,departDate);
	}
}
