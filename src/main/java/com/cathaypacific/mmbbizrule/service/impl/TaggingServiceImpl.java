package com.cathaypacific.mmbbizrule.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.CacheNamesConstants;
import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.db.dao.AncillaryBannerDeeplinkDataDAO;
import com.cathaypacific.mmbbizrule.db.dao.MealTagMappingDAO;
import com.cathaypacific.mmbbizrule.db.dao.RedemptionSubclassUpgradeDAO;
import com.cathaypacific.mmbbizrule.db.model.AncillaryBannerDeeplinkData;
import com.cathaypacific.mmbbizrule.db.model.MealTagMappingModel;
import com.cathaypacific.mmbbizrule.service.TaggingService;

@Service
public class TaggingServiceImpl implements TaggingService {

	@Autowired
	private MealTagMappingDAO mealTagMappingDAO;
	
	@Autowired
	private AncillaryBannerDeeplinkDataDAO ancillaryBannerDeeplinkDataDAO;
	
	@Autowired
	private RedemptionSubclassUpgradeDAO redemptionSubclassUpgradeDAO;
	
	public List<MealTagMappingModel> findMealTagMapping(){
		return mealTagMappingDAO.findByAppCode(MMBConstants.APP_CODE);
	}
	
	public List<AncillaryBannerDeeplinkData> findAncillaryBannerDeeplinkData(){
		return ancillaryBannerDeeplinkDataDAO.findByAppCode(MMBConstants.APP_CODE);
	}

	@Override
	@Cacheable(cacheNames=CacheNamesConstants.REDEMPTION_SUBCLASS, keyGenerator = "shareKeyGenerator")
	public List<String> findSubclassList(boolean upgrade) {
		return redemptionSubclassUpgradeDAO.getSubclassList(MMBConstants.APP_CODE, upgrade);
	}

}
