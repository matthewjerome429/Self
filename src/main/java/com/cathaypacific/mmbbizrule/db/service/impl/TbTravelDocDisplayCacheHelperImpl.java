package com.cathaypacific.mmbbizrule.db.service.impl;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocDisplayCacheHelper;

@Service
public class TbTravelDocDisplayCacheHelperImpl implements TbTravelDocDisplayCacheHelper {

	@Autowired
	private OLCIServiceV2 olciServiceV2;

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.TRAVEL_DOC_DISPLAY, keyGenerator = "shareKeyGenerator")
	public List<TravelDocDisplay> findAll() {

		return olciServiceV2.retrieveTravelDocDisplayFromDB().getTBTravelDocDisplays();
	}
}
