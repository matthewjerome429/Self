package com.cathaypacific.mmbbizrule.db.service.impl;

import java.sql.Date;
import java.util.*;

import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocOD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocOdCacheHelper;

@Service
public class TbTravelDocOdCacheHelperImpl implements TbTravelDocOdCacheHelper {

	@Autowired
	private OLCIServiceV2 olciServiceV2;

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.TRAVEL_DOC_OD, keyGenerator = "shareKeyGenerator")
	public int findTravelDocVersion(String appCode, List<String> origins, List<String> destinations) {
		return olciServiceV2.retrieveTravelDocVersionFromDB(appCode,origins,destinations);
	}

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.TRAVEL_DOC_OD, keyGenerator = "shareKeyGenerator")
	public List<TravelDocOD> findByAppCodeInAndStartDateBefore(Collection<String> appCode, Date startDate) {
		return olciServiceV2.retrieveTravelDocODByAppCodeInAndStartDateBeforeFromDB(appCode,startDate).getTravelDocODs();
	}


}
