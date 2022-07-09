package com.cathaypacific.mmbbizrule.service;

import java.util.List;

import com.cathaypacific.mmbbizrule.db.model.AncillaryBannerDeeplinkData;
import com.cathaypacific.mmbbizrule.db.model.MealTagMappingModel;

public interface TaggingService {

	public List<MealTagMappingModel> findMealTagMapping();
	
	public List<AncillaryBannerDeeplinkData> findAncillaryBannerDeeplinkData();

	public List<String> findSubclassList(boolean b);

}
