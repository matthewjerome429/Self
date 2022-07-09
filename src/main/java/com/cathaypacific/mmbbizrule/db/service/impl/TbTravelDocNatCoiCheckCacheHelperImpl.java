package com.cathaypacific.mmbbizrule.db.service.impl;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mmbbizrule.cxservice.olci_v2.service.OLCIServiceV2;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocNatCoiCheckCacheHelper;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocNatCoiMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class TbTravelDocNatCoiCheckCacheHelperImpl implements TbTravelDocNatCoiCheckCacheHelper {

	@Autowired
	private OLCIServiceV2 olciServiceV2;

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.TRAVEL_DOC_NAT_COI_CHECK, keyGenerator = "shareKeyGenerator")
	public List<TravelDocNatCoiMapping> findDocCoisByAppCode(String appCode) {
		return olciServiceV2.retrieveDocCoisByAppCode(appCode).getTbTravelDocNatCoiMappings();
	}
}
