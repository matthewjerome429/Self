package com.cathaypacific.mmbbizrule.db.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;


import com.cathaypacific.olciconsumer.model.response.db.TravelDocList;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocListCacheHelper;

@Service
public class TbTravelDocListCacheHelperImpl implements TbTravelDocListCacheHelper {

	@Autowired
	private OLCIServiceV2 olciServiceV2;

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.TRAVEL_DOC_LIST, keyGenerator = "shareKeyGenerator")
	public List<String> findTravelDocCodeByVersion(int travelDocVersion, List<String> travelDocTypes) {
		return olciServiceV2.retrieveTravelDocCodeByVersionFromDB(travelDocVersion,travelDocTypes).getTravelDocCodes();
	}

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.TRAVEL_DOC_LIST, keyGenerator = "shareKeyGenerator")
	public List<Integer> findDocVersionByCodeAndType(String travelDocCode, String travelDocType) {
		Type listType = new TypeToken<List<Integer>>() {}.getType();
		return olciServiceV2.retrieveDocVersionByCodeAndTypeFromDB(travelDocCode,travelDocType).getDocVersions();
	}

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.TRAVEL_DOC_LIST, keyGenerator = "shareKeyGenerator")
	public List<String> findVersionByTypeGroupByCode(String travelDocType) {
		return olciServiceV2.retrieveVersionByTypeGroupByCodeFromDB(travelDocType).getVersions();
	}

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.TRAVEL_DOC_LIST, keyGenerator = "shareKeyGenerator")
	public List<TravelDocList> findAll() {
		Type listType = new TypeToken<List<TravelDocList>>() {}.getType();
		return olciServiceV2.retrieveTravelDocListFromDB().getTbTravelDocLists();
	}
}
